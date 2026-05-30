import axios from 'axios'
import { getToken, clearAuth } from './auth'

// In dev: Vite proxy forwards /api → localhost:8080 (no VITE_API_URL needed)
// In prod: VITE_API_URL is set to the Railway backend URL, e.g. https://your-app.railway.app/api
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  timeout: 15000
})

api.interceptors.request.use((config) => {
  const token = getToken()
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      clearAuth()
      window.location.href = '/login'
    }
    return Promise.reject(err)
  }
)

export const login = (username, password) =>
  api.post('/auth/login', { username, password })

export const getDriftBoard = (params = {}) =>
  api.get('/drift/board', { params })

export const exportCsv = async (params = {}) => {
  const res = await api.get('/drift/export/csv', {
    params,
    responseType: 'blob'
  })
  const url = URL.createObjectURL(new Blob([res.data], { type: 'text/csv' }))
  const a = document.createElement('a')
  a.href = url
  a.setAttribute('download', 'concall-drift-export.csv')
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
  URL.revokeObjectURL(url)
}

export default api
