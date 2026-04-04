const API = '/api';

function navigateTo(url) {
    document.body.classList.add('fade-out');
    setTimeout(() => {
        window.location.href = url;
    }, 50);
}

// Intercept standard link clicks
document.addEventListener('click', e => {
    const link = e.target.closest('a');
    if (link && link.href && link.target !== '_blank' && !link.href.startsWith('javascript:') && !link.href.startsWith('#')) {
        e.preventDefault();
        navigateTo(link.href);
    }
});

function checkAuth(requireAuth = true) {
    const token = localStorage.getItem('token');
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    
    if (requireAuth && (!token || !user)) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        navigateTo('index.html');
        return null;
    }
    if (!requireAuth && token && user) {
        navigateTo('dashboard.html');
        return null;
    }
    return { token, user };
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    navigateTo('index.html');
}

async function apiFetch(endpoint, options = {}) {
    const token = localStorage.getItem('token');
    const headers = {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        ...options.headers
    };

    const res = await fetch(`${API}${endpoint}`, { ...options, headers });
    const data = await res.json();
    
    if (!res.ok || !data.success) {
        throw new Error(data.message || 'API request failed');
    }
    return data.data; // Since ApiResponse uses { success, message, data }
}

function initTheme() {
    const savedTheme = localStorage.getItem('theme') || 'light';
    document.documentElement.setAttribute('data-theme', savedTheme);
}

function toggleTheme() {
    const current = document.documentElement.getAttribute('data-theme');
    const newTheme = current === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', newTheme);
    localStorage.setItem('theme', newTheme);
}

// Call initTheme immediately so the theme applies before rendering
initTheme();

function renderSidebar(activeItem) {
    // 1. Create Sidebar
    const sidebar = `
        <div class="app-sidebar" id="sidebar">
            <div class="sidebar-header">
                <div class="brand">
                    <div class="brand-icon">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2v20"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg>
                    </div>
                    FairSplit
                </div>
            </div>
            <div class="sidebar-nav">
                <p style="font-size: 0.7rem; font-weight: 600; color: var(--text-muted); text-transform: uppercase; padding: 0.5rem 1rem 0.2rem;">Menu</p>
                <a href="dashboard.html" class="${activeItem === 'dashboard' ? 'active' : ''}">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect width="7" height="9" x="3" y="3" rx="1"/><rect width="7" height="5" x="14" y="3" rx="1"/><rect width="7" height="9" x="14" y="12" rx="1"/><rect width="7" height="5" x="3" y="16" rx="1"/></svg>
                    Dashboard
                </a>
                <a href="groups.html" class="${activeItem === 'groups' ? 'active' : ''}">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
                    Groups
                </a>
            </div>
        </div>
    `;
    document.body.insertAdjacentHTML('afterbegin', sidebar);

    // 2. Wrap Main Content and add Top Header
    const mainContent = document.querySelector('.main-content');
    if (mainContent) {
        const wrapper = document.createElement('div');
        wrapper.className = 'app-wrapper';
        mainContent.parentNode.insertBefore(wrapper, mainContent);
        wrapper.appendChild(mainContent);

        const header = `
            <div class="app-header">
                <div style="display: flex; align-items: center; gap: 1rem;">
                    <button class="mobile-toggle" onclick="document.getElementById('sidebar').classList.toggle('open')">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="3" y1="12" x2="21" y2="12"/><line x1="3" y1="6" x2="21" y2="6"/><line x1="3" y1="18" x2="21" y2="18"/></svg>
                    </button>
                    <div class="header-search">
                        <input type="text" placeholder="Search groups, expenses...">
                    </div>
                </div>
                <div class="header-actions">
                    <button onclick="toggleTheme()" class="btn-icon" title="Toggle Theme" style="background: var(--input-bg); border: 1px solid var(--border); border-radius: 50%; width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; padding: 0;">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="5"/><path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"/></svg>
                    </button>
                    <button onclick="logout()" class="btn-icon" title="Logout" style="background: var(--input-bg); border: 1px solid var(--border); border-radius: 50%; width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; padding: 0;">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
                    </button>
                    <div style="width: 1px; height: 24px; background: var(--border); margin: 0 0.5rem;"></div>
                    <div style="display: flex; align-items: center; gap: 0.75rem;">
                        <div style="text-align: right;">
                            <div style="font-size: 0.85rem; font-weight: 600;" id="header-user">User</div>
                            <div style="font-size: 0.75rem; color: var(--text-muted);">Member</div>
                        </div>
                        <div id="header-avatar" style="width: 36px; height: 36px; border-radius: 50%; background: var(--primary); color: white; display: flex; align-items: center; justify-content: center; font-weight: 600; font-size: 1rem;">U</div>
                    </div>
                </div>
            </div>
        `;
        wrapper.insertAdjacentHTML('afterbegin', header);
    }

    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if (user.name) {
        const elName = document.getElementById('header-user');
        const elAvatar = document.getElementById('header-avatar');
        if(elName) elName.textContent = user.name;
        if(elAvatar) elAvatar.textContent = user.name.charAt(0).toUpperCase();
    }
}

// Ensure toggleTheme is defined (it is earlier in the file)

