const API = 'http://localhost:8080/api';

function getToken() { return localStorage.getItem('token'); }
function getUsername() { return localStorage.getItem('username'); }
function getRole() { return localStorage.getItem('role'); }

function authHeaders(extra = {}) {
  return { 'Content-Type': 'application/json', 'Authorization': `Bearer ${getToken()}`, ...extra };
}

async function apiFetch(path, options = {}) {
  const res = await fetch(API + path, {
    headers: authHeaders(),
    ...options,
  });
  if (res.status === 401) { logout(); return; }
  if (!res.ok) throw new Error(await res.text());
  const text = await res.text();
  return text ? JSON.parse(text) : null;
}

async function apiPost(path, body) {
  return apiFetch(path, { method: 'POST', body: JSON.stringify(body) });
}

async function apiDelete(path) {
  return apiFetch(path, { method: 'DELETE' });
}

function logout() {
  localStorage.clear();
  window.location.href = '/login.html';
}

function requireAuth() {
  if (!getToken()) window.location.href = '/login.html';
}

/* ── Sidebar helpers ─────────────────────────── */
function initSidebar(activePage) {
  requireAuth();

  // user info
  const uname = getUsername() || 'User';
  const role  = getRole() || '';
  document.getElementById('user-avatar').textContent = uname[0].toUpperCase();
  document.getElementById('user-name').textContent   = uname;
  document.getElementById('user-role').textContent   = role;
  document.getElementById('btn-logout').onclick = logout;

  // active link
  document.querySelectorAll('.sidebar-nav a').forEach(a => {
    if (a.dataset.page === activePage) a.classList.add('active');
  });

  // hamburger
  const ham     = document.getElementById('hamburger');
  const sidebar = document.getElementById('sidebar');
  const overlay = document.getElementById('sidebar-overlay');
  if (ham) {
    ham.onclick     = () => { sidebar.classList.add('open'); overlay.classList.add('open'); };
    overlay.onclick = () => { sidebar.classList.remove('open'); overlay.classList.remove('open'); };
  }
}

/* ── Modal helpers ───────────────────────────── */
function openModal(id)  { document.getElementById(id).style.display = 'flex'; }
function closeModal(id) { document.getElementById(id).style.display = 'none'; }

/* ── Badge helpers ───────────────────────────── */
function statusBadge(status) {
  const map = { OPEN: 'badge-open', IN_PROGRESS: 'badge-in_progress', CLOSED: 'badge-closed' };
  return `<span class="badge ${map[status] || 'badge-default'}">${(status||'').replace('_',' ')}</span>`;
}

function roleBadge(role) {
  return `<span class="badge badge-${(role||'user').toLowerCase()}">${role||''}</span>`;
}

/* ── Table state helpers ─────────────────────── */
function showLoading(tbodyId, cols) {
  document.getElementById(tbodyId).innerHTML =
    `<tr><td colspan="${cols}"><div class="state-box"><div class="spinner"></div><p>Loading…</p></div></td></tr>`;
}

function showEmpty(tbodyId, cols, icon, msg) {
  document.getElementById(tbodyId).innerHTML =
    `<tr><td colspan="${cols}"><div class="state-box"><div class="state-icon">${icon}</div><p>${msg}</p></div></td></tr>`;
}
