import { useState, useEffect, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { getDriftBoard, exportCsv } from '../services/api'
import { getUser, clearAuth } from '../services/auth'
import DriftBoard from '../components/DriftBoard'
import FilterBar from '../components/FilterBar'

const REFRESH_INTERVAL = 30000

export default function DashboardPage() {
  const navigate  = useNavigate()
  const user      = getUser()

  const [rows,        setRows]        = useState([])
  const [loading,     setLoading]     = useState(true)
  const [error,       setError]       = useState('')
  const [lastRefresh, setLastRefresh] = useState(null)
  const [filters,     setFilters]     = useState({ exchange: '', fromDate: '', toDate: '' })
  const [sortConfig,  setSortConfig]  = useState({ key: 'concallDate', dir: 'desc' })

  // Always holds the latest filters — used by the auto-refresh timer
  const filtersRef = useRef(filters)
  filtersRef.current = filters

  // ── Core fetch function (plain async, no hooks) ───────────────────────────
  async function fetchBoard(f) {
    setLoading(true)
    setError('')
    try {
      const params = {}
      if (f.exchange) params.exchange = f.exchange
      if (f.fromDate) params.fromDate = f.fromDate
      if (f.toDate)   params.toDate   = f.toDate
      const res = await getDriftBoard(params)
      setRows(res.data.data || [])
      setLastRefresh(new Date())
    } catch {
      setError('Failed to load drift data. Retrying…')
    } finally {
      setLoading(false)
    }
  }

  // Initial load on mount only
  useEffect(() => {
    fetchBoard({ exchange: '', fromDate: '', toDate: '' })
  }, []) // eslint-disable-line react-hooks/exhaustive-deps

  // Auto-refresh every 30 s using the ref so the timer is created only once
  useEffect(() => {
    const timer = setInterval(() => fetchBoard(filtersRef.current), REFRESH_INTERVAL)
    return () => clearInterval(timer)
  }, []) // eslint-disable-line react-hooks/exhaustive-deps

  // ── Handlers ──────────────────────────────────────────────────────────────
  function handleFilter(newFilters) {
    setFilters(newFilters)
    fetchBoard(newFilters)      // explicit call — no stale closure possible
  }

  function handleSort(key) {
    setSortConfig(prev => ({
      key,
      dir: prev.key === key && prev.dir === 'asc' ? 'desc' : 'asc'
    }))
  }

  function handleExport() {
    const params = {}
    if (filters.exchange) params.exchange = filters.exchange
    if (filters.fromDate) params.fromDate = filters.fromDate
    if (filters.toDate)   params.toDate   = filters.toDate
    exportCsv(params)
  }

  function handleLogout() {
    clearAuth()
    navigate('/login')
  }

  // ── Sorting (client-side) ─────────────────────────────────────────────────
  const sortedRows = [...rows].sort((a, b) => {
    let av = a[sortConfig.key]
    let bv = b[sortConfig.key]
    if (av == null) return 1
    if (bv == null) return -1
    if (typeof av === 'string') av = av.toLowerCase()
    if (typeof bv === 'string') bv = bv.toLowerCase()
    if (av < bv) return sortConfig.dir === 'asc' ? -1 : 1
    if (av > bv) return sortConfig.dir === 'asc' ? 1 : -1
    return 0
  })

  // ── Render ────────────────────────────────────────────────────────────────
  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <div className="header-left">
          <span className="header-icon">&#9650;</span>
          <div>
            <h1 className="header-title">Post-Concall Announcement Drift</h1>
            <div className="header-badges">
              <span className="badge badge-nse">NSE</span>
              <span className="badge-dot">·</span>
              <span className="badge badge-bse">BSE</span>
              <span className="header-year">2026</span>
            </div>
          </div>
        </div>
        <div className="header-right">
          {lastRefresh && (
            <span className="refresh-time">
              <span className="live-dot"></span>
              Updated {lastRefresh.toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit' })}
            </span>
          )}
          <button className="btn-export" onClick={handleExport}>
            &#8595; Export CSV
          </button>
          <div className="user-info">
            <span className="user-name">{user?.username}</span>
            <button className="btn-logout" onClick={handleLogout}>Sign out</button>
          </div>
        </div>
      </header>

      <div className="dashboard-body">
        <div className="board-description">
          <p>Tracking stock drift relative to concall-announcement baselines across NSE &amp; BSE.
          Prices auto-refresh every 30 seconds.</p>
        </div>

        <FilterBar filters={filters} onFilter={handleFilter} />

        {error && <div className="board-error">{error}</div>}

        {loading ? (
          <div className="board-loading">
            <div className="spinner"></div>
            <span>Loading drift data…</span>
          </div>
        ) : (
          <DriftBoard rows={sortedRows} sortConfig={sortConfig} onSort={handleSort} />
        )}

        <div className="board-footer">
          <span>{rows.length} concall event{rows.length !== 1 ? 's' : ''} tracked</span>
          <span className="footer-note">Baselines anchored at announcement time · IST timezone</span>
        </div>
      </div>
    </div>
  )
}
