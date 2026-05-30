import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { login } from '../services/api'
import { setToken, setUser } from '../services/auth'

export default function LoginPage() {
  const navigate = useNavigate()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await login(username, password)
      const { token, username: u, role } = res.data.data
      setToken(token)
      setUser({ username: u, role })
      navigate('/dashboard')
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid credentials. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-bg">
      <div className="login-card">
        <div className="login-header">
          <div className="login-logo">
            <span className="login-logo-icon">&#9650;</span>
          </div>
          <h1 className="login-title">Post-Concall Announcement Drift</h1>
          <div className="login-badges">
            <span className="badge badge-nse">NSE</span>
            <span className="badge-dot">·</span>
            <span className="badge badge-bse">BSE</span>
            <span className="badge-year">2026</span>
          </div>
          <p className="login-desc">
            A live board tracking how Indian stocks move in the hours and days
            after an earnings concall. Baselines are anchored at announcement
            time and compared against real-time market activity.
          </p>
          <ul className="login-features">
            <li><span className="feature-dot"></span>Drift tracking relative to a concall-anchored reference point</li>
            <li><span className="feature-dot"></span>Result &amp; concall events across NSE/BSE stocks</li>
            <li><span className="feature-dot"></span>Live pricing with filtering and CSV export</li>
          </ul>
        </div>

        <form className="login-form" onSubmit={handleSubmit}>
          <h2 className="signin-label">Sign in</h2>
          {error && <div className="login-error">{error}</div>}
          <div className="form-group">
            <label className="form-label">Username</label>
            <input
              className="form-input"
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter username"
              autoFocus
              required
            />
          </div>
          <div className="form-group">
            <label className="form-label">Password</label>
            <input
              className="form-input"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter password"
              required
            />
          </div>
          <button className="btn-signin" type="submit" disabled={loading}>
            {loading ? 'Signing in…' : 'Sign in'}
          </button>
          <p className="login-note">
            Access is invitation-only — accounts are provisioned by the operator.
            There is no public sign-up.
          </p>
        </form>
      </div>
    </div>
  )
}
