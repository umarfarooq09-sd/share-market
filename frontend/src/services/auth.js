const TOKEN_KEY = 'concall_drift_token'
const USER_KEY = 'concall_drift_user'

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token)
}

export function getUser() {
  const u = localStorage.getItem(USER_KEY)
  return u ? JSON.parse(u) : null
}

export function setUser(user) {
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}

export function clearAuth() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}
