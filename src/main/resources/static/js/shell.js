/* Injects the sidebar + topbar into .app-shell pages */
function buildShell(activePage, pageTitle) {
  const nav = [
    { page: 'dashboard',  href: '/index.html',      icon: '🏠', label: 'Dashboard'  },
    { page: 'cases',      href: '/cases.html',       icon: '⚖️', label: 'Cases'      },
    { page: 'clients',    href: '/clients.html',     icon: '👥', label: 'Clients'    },
    { page: 'lawyers',    href: '/lawyers.html',     icon: '👨‍⚖️', label: 'Lawyers'    },
    { page: 'documents',  href: '/documents.html',   icon: '📄', label: 'Documents'  },
    { page: 'payments',   href: '/payments.html',    icon: '💳', label: 'Payments'   },
    { page: 'users',      href: '/users.html',       icon: '🔑', label: 'Users'      },
    { page: 'audit',      href: '/audit.html',       icon: '📋', label: 'Audit Logs' },
    { page: 'tax',        href: '/tax.html',         icon: '🧾', label: 'Tax Report' },
    { page: 'myfiles',    href: '/myfiles.html',     icon: '📚', label: 'My Library' },
  ];

  const links = nav.map(n =>
    `<a href="${n.href}" data-page="${n.page}" class="${n.page === activePage ? 'active' : ''}">
       <span class="nav-icon">${n.icon}</span>${n.label}
     </a>`
  ).join('');

  document.getElementById('sidebar').innerHTML = `
    <div class="sidebar-logo">
      <h1>⚖️ Casa Legal Hub</h1>
      <span>Legal Management System</span>
    </div>
    <nav class="sidebar-nav">
      <div class="nav-section">Menu</div>
      ${links}
    </nav>
    <div class="sidebar-footer">
      <div class="user-info">
        <div class="user-avatar" id="user-avatar"></div>
        <div class="user-details">
          <strong id="user-name"></strong>
          <small id="user-role"></small>
        </div>
      </div>
      <button class="btn-logout" id="btn-logout">🚪 Logout</button>
    </div>
  `;

  document.getElementById('topbar-title').textContent = pageTitle;
  initSidebar(activePage);
}
