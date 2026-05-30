export default function DriftCell({ value, bold }) {
  if (value == null) {
    return <td className="td td-drift td-na">—</td>
  }
  const num = Number(value)
  const cls = num > 0 ? 'drift-pos' : num < 0 ? 'drift-neg' : 'drift-flat'
  const sign = num > 0 ? '+' : ''
  return (
    <td className={`td td-drift ${cls} ${bold ? 'drift-bold' : ''}`}>
      {sign}{num.toFixed(2)}%
    </td>
  )
}
