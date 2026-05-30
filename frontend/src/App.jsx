import { Routes, Route, Navigate } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import DashboardPage from './pages/DashboardPage'
import { getToken } from './services/auth'

function PrivateRoute({ children }) {
  return getToken() ? children : <Navigate to="/login" replace />
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/dashboard" element={<PrivateRoute><DashboardPage /></PrivateRoute>} />
      <Route path="*" element={<Navigate to={getToken() ? '/dashboard' : '/login'} replace />} />
    </Routes>
  )
}
