import { useState, useEffect } from 'react'

export default function FilterBar({ filters, onFilter }) {
  const [local, setLocal] = useState(filters)

  // Keep local inputs in sync if parent resets filters
  useEffect(() => {
    setLocal(filters)
  }, [filters])

  function handleChange(e) {
    setLocal(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  function handleApply(e) {
    e.preventDefault()
    onFilter(local)
  }

  function handleReset() {
    const reset = { exchange: '', fromDate: '', toDate: '' }
    setLocal(reset)
    onFilter(reset)
  }

  return (
    <form className="filter-bar" onSubmit={handleApply}>
      <div className="filter-group">
        <label className="filter-label">Exchange</label>
        <select
          className="filter-select"
          name="exchange"
          value={local.exchange}
          onChange={handleChange}
        >
          <option value="">All</option>
          <option value="NSE">NSE</option>
          <option value="BSE">BSE</option>
        </select>
      </div>

      <div className="filter-group">
        <label className="filter-label">Concall From</label>
        <input
          className="filter-input"
          type="date"
          name="fromDate"
          value={local.fromDate}
          onChange={handleChange}
        />
      </div>

      <div className="filter-group">
        <label className="filter-label">Concall To</label>
        <input
          className="filter-input"
          type="date"
          name="toDate"
          value={local.toDate}
          onChange={handleChange}
        />
      </div>

      <div className="filter-actions">
        <button className="btn-apply" type="submit">Apply</button>
        <button className="btn-reset" type="button" onClick={handleReset}>Reset</button>
      </div>
    </form>
  )
}
