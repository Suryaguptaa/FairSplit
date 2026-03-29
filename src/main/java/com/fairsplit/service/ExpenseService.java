package com.fairsplit.service;

import com.fairsplit.dto.*;
import com.fairsplit.entity.AppGroup;
import com.fairsplit.entity.GroupExpense;
import com.fairsplit.entity.Settlement;
import com.fairsplit.entity.auth.AppUser;
import com.fairsplit.repository.AppGroupRepository;
import com.fairsplit.repository.GroupExpenseRepository;
import com.fairsplit.repository.SettlementRepository;
import com.fairsplit.repository.auth.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private GroupExpenseRepository expenseRepository;

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private AppGroupRepository groupRepository;

    @Autowired
    private AppUserRepository userRepository;

    private AppUser getLoggedInUser() {
        AppUser principal = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ── Add Expense ──────────────────────────────────────────────────────────

    @Transactional
    public GroupExpenseDTO addExpense(GroupExpenseDTO dto) {
        AppUser user = getLoggedInUser();

        AppGroup group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getMembers().contains(user)) {
            throw new RuntimeException("You are not a member of this group");
        }

        AppUser paidBy = userRepository.findById(dto.getPaidBy().getId())
                .orElseThrow(() -> new RuntimeException("Payer not found"));

        GroupExpense expense = new GroupExpense();
        expense.setTitle(dto.getTitle());
        expense.setAmount(dto.getAmount());
        expense.setCategory(dto.getCategory());
        expense.setDate(dto.getDate() != null ? dto.getDate() : LocalDateTime.now());
        expense.setGroup(group);
        expense.setPaidBy(paidBy);

        for (UserDTO u : dto.getSplitBetween()) {
            AppUser member = userRepository.findById(u.getId())
                    .orElseThrow(() -> new RuntimeException("Split member not found: " + u.getId()));
            expense.getSplitBetween().add(member);
        }

        GroupExpense saved = expenseRepository.save(expense);
        return mapToDTO(saved);
    }

    // ── Get Expenses for a Group ──────────────────────────────────────────────

    public List<GroupExpenseDTO> getGroupExpenses(Long groupId) {
        AppGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return expenseRepository.findByGroupOrderByDateDesc(group)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ── Edit Expense ─────────────────────────────────────────────────────────

    @Transactional
    public GroupExpenseDTO editExpense(Long expenseId, GroupExpenseDTO dto) {
        AppUser user = getLoggedInUser();

        GroupExpense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getGroup().getMembers().contains(user)) {
            throw new RuntimeException("You are not a member of this group");
        }

        AppUser paidBy = userRepository.findById(dto.getPaidBy().getId())
                .orElseThrow(() -> new RuntimeException("Payer not found"));

        expense.setTitle(dto.getTitle());
        expense.setAmount(dto.getAmount());
        expense.setCategory(dto.getCategory());
        expense.setPaidBy(paidBy);

        expense.getSplitBetween().clear();
        for (UserDTO u : dto.getSplitBetween()) {
            AppUser member = userRepository.findById(u.getId())
                    .orElseThrow(() -> new RuntimeException("Split member not found"));
            expense.getSplitBetween().add(member);
        }

        return mapToDTO(expenseRepository.save(expense));
    }

    // ── Delete Expense ───────────────────────────────────────────────────────

    @Transactional
    public void deleteExpense(Long expenseId) {
        AppUser user = getLoggedInUser();

        GroupExpense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getGroup().getMembers().contains(user)) {
            throw new RuntimeException("You are not a member of this group");
        }

        expenseRepository.delete(expense);
    }

    // ── Record a Settlement ──────────────────────────────────────────────────

    @Transactional
    public SettlementDTO addSettlement(SettlementDTO dto) {
        AppGroup group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        AppUser paidBy = userRepository.findById(dto.getPaidBy().getId())
                .orElseThrow(() -> new RuntimeException("Payer not found"));

        AppUser paidTo = userRepository.findById(dto.getPaidTo().getId())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        Settlement settlement = new Settlement();
        settlement.setAmount(dto.getAmount());
        settlement.setDate(LocalDateTime.now());
        settlement.setGroup(group);
        settlement.setPaidBy(paidBy);
        settlement.setPaidTo(paidTo);

        Settlement saved = settlementRepository.save(settlement);

        SettlementDTO result = new SettlementDTO();
        result.setId(saved.getId());
        result.setAmount(saved.getAmount());
        return result;
    }

    // ── Calculate Balances ───────────────────────────────────────────────────

    public List<BalanceDTO> getBalances(Long groupId) {
        AppGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        Map<Long, Double> balances = new HashMap<>();
        for (AppUser u : group.getMembers()) {
            balances.put(u.getId(), 0.0);
        }

        // Add what each person paid, subtract their share
        for (GroupExpense ex : expenseRepository.findByGroupOrderByDateDesc(group)) {
            Long payerId = ex.getPaidBy().getId();
            balances.merge(payerId, ex.getAmount(), Double::sum);

            double share = ex.getAmount() / ex.getSplitBetween().size();
            for (AppUser u : ex.getSplitBetween()) {
                balances.merge(u.getId(), -share, Double::sum);
            }
        }

        // Account for settlements already made
        for (Settlement s : settlementRepository.findByGroupOrderByDateDesc(group)) {
            balances.merge(s.getPaidBy().getId(), s.getAmount(), Double::sum);
            balances.merge(s.getPaidTo().getId(), -s.getAmount(), Double::sum);
        }

        return balances.entrySet().stream().map(entry -> {
            AppUser u = userRepository.findById(entry.getKey()).orElseThrow();
            double rounded = BigDecimal.valueOf(entry.getValue())
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            return new BalanceDTO(new UserDTO(u.getId(), u.getName(), u.getEmail()), rounded);
        }).collect(Collectors.toList());
    }

    // ── Simplified Debts (who pays whom to settle everything) ────────────────

    public List<Map<String, Object>> getSimplifiedDebts(Long groupId) {
        List<BalanceDTO> balances = getBalances(groupId);

        List<BalanceDTO> debtors = balances.stream()
                .filter(b -> b.getNetBalance() < -0.01)
                .collect(Collectors.toList());
        List<BalanceDTO> creditors = balances.stream()
                .filter(b -> b.getNetBalance() > 0.01)
                .collect(Collectors.toList());

        List<Map<String, Object>> debts = new ArrayList<>();
        int i = 0, j = 0;

        while (i < debtors.size() && j < creditors.size()) {
            BalanceDTO debtor = debtors.get(i);
            BalanceDTO creditor = creditors.get(j);

            double amount = Math.min(-debtor.getNetBalance(), creditor.getNetBalance());
            double rounded = BigDecimal.valueOf(amount)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            Map<String, Object> debt = new HashMap<>();
            debt.put("from", debtor.getUser());
            debt.put("to", creditor.getUser());
            debt.put("amount", rounded);
            debts.add(debt);

            debtor.setNetBalance(debtor.getNetBalance() + amount);
            creditor.setNetBalance(creditor.getNetBalance() - amount);

            if (debtor.getNetBalance() >= -0.01) i++;
            if (creditor.getNetBalance() <= 0.01) j++;
        }

        return debts;
    }

    // ── Mapper ───────────────────────────────────────────────────────────────

    private GroupExpenseDTO mapToDTO(GroupExpense ex) {
        GroupExpenseDTO dto = new GroupExpenseDTO();
        dto.setId(ex.getId());
        dto.setTitle(ex.getTitle());
        dto.setAmount(ex.getAmount());
        dto.setCategory(ex.getCategory());
        dto.setDate(ex.getDate());
        dto.setGroupId(ex.getGroup().getId());
        dto.setPaidBy(new UserDTO(ex.getPaidBy().getId(), ex.getPaidBy().getName(), ex.getPaidBy().getEmail()));
        dto.setSplitBetween(ex.getSplitBetween().stream()
                .map(m -> new UserDTO(m.getId(), m.getName(), m.getEmail()))
                .collect(Collectors.toList()));
        return dto;
    }
}
