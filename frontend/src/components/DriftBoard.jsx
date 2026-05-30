import DriftCell from './DriftCell'

const COLUMNS = [
  { key: 'symbol',       label: 'Symbol',        sortable: true  },
  { key: 'companyName',  label: 'Company',        sortable: true  },
  { key: 'exchange',     label: 'Exch',           sortable: true  },
  { key: 'sector',       label: 'Sector',         sortable: true  },
  { key: 'quarter',      label: 'Quarter',        sortable: false },
  { key: 'concallDate',  label: 'Concall Date',   sortable: true  },
  { key: 'baselinePrice',label: 'Baseline ₹',     sortable: true  },
  { key: 'drift1h',      label: '1H Drift',       sortable: true  },
  { key: 'drift4h',      label: '4H Drift',       sortable: true  },
  { key: 'drift1d',      label: '1D Drift',       sortable: true  },
  { key: 'drift2d',      label: '2D Drift',       sortable: true  },
  { key: 'drift5d',      label: '5D Drift',       sortable: true  },
  { key: 'currentPrice', label: 'Current ₹',      sortable: true  },
  { key: 'driftCurrent', label: 'Now Drift',      sortable: true  },
]

function SortArrow({ col, sortConfig }) {
  if (!col.sortable) return null
  if (sortConfig.key !== col.key) return <span className="sort-neutral">⇅</span>
  return sortConfig.dir === 'asc'
    ? <span className="sort-active">▲</span>
    : <span className="sort-active">▼</span>
}

export default function DriftBoard({ rows, sortConfig, onSort }) {
  if (rows.length === 0) {
    return (
      <div className="board-empty">
        <p>No concall events found for the selected filters.</p>
      </div>
    )
  }

  return (
    <div className="board-scroll">
      <table className="drift-table">
        <thead>
          <tr>
            {COLUMNS.map(col => (
              <th
                key={col.key}
                className={`th ${col.sortable ? 'th-sortable' : ''}`}
                onClick={() => col.sortable && onSort(col.key)}
              >
                {col.label} <SortArrow col={col} sortConfig={sortConfig} />
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map(row => (
            <tr key={row.concallEventId} className="board-row">
              <td className="td td-symbol">{row.symbol}</td>
              <td className="td td-company" title={row.companyName}>
                {row.companyName.length > 22 ? row.companyName.slice(0, 22) + '…' : row.companyName}
              </td>
              <td className="td">
                <span className={`badge badge-${row.exchange?.toLowerCase()}`}>{row.exchange}</span>
              </td>
              <td className="td td-sector">{row.sector}</td>
              <td className="td td-center">
                <span className="quarter-tag">{row.quarter} {row.fiscalYear}</span>
              </td>
              <td className="td td-center">{formatDate(row.concallDate)}</td>
              <td className="td td-price">₹{formatNum(row.baselinePrice)}</td>
              <DriftCell value={row.drift1h} />
              <DriftCell value={row.drift4h} />
              <DriftCell value={row.drift1d} />
              <DriftCell value={row.drift2d} />
              <DriftCell value={row.drift5d} />
              <td className="td td-price">₹{formatNum(row.currentPrice)}</td>
              <DriftCell value={row.driftCurrent} bold />
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

function formatDate(dateStr) {
  if (!dateStr) return '—'
  const d = new Date(dateStr)
  return d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' })
}

function formatNum(val) {
  if (val == null) return '—'
  return Number(val).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}
