-- ================================================================
-- Seed data for Post-Concall Announcement Drift
-- Users are created by DataInitializer.java (BCrypt encoded at startup)
-- ================================================================

-- Stocks
INSERT INTO stocks (symbol, company_name, exchange, sector, created_at) VALUES
('RELIANCE',   'Reliance Industries Ltd',        'NSE', 'Energy',          NOW()),
('TCS',        'Tata Consultancy Services Ltd',  'NSE', 'IT',              NOW()),
('HDFCBANK',   'HDFC Bank Ltd',                  'NSE', 'Banking',         NOW()),
('INFY',       'Infosys Ltd',                    'NSE', 'IT',              NOW()),
('ICICIBANK',  'ICICI Bank Ltd',                 'NSE', 'Banking',         NOW()),
('BHARTIARTL', 'Bharti Airtel Ltd',              'NSE', 'Telecom',         NOW()),
('WIPRO',      'Wipro Ltd',                      'NSE', 'IT',              NOW()),
('SBIN',       'State Bank of India',            'NSE', 'Banking',         NOW()),
('LT',         'Larsen & Toubro Ltd',            'NSE', 'Infrastructure',  NOW()),
('HCLTECH',    'HCL Technologies Ltd',           'NSE', 'IT',              NOW()),
('TATAMOTORS', 'Tata Motors Ltd',                'BSE', 'Auto',            NOW()),
('ADANIENT',   'Adani Enterprises Ltd',          'NSE', 'Conglomerate',    NOW()),
('MARUTI',     'Maruti Suzuki India Ltd',        'BSE', 'Auto',            NOW()),
('SUNPHARMA',  'Sun Pharmaceutical Industries',  'NSE', 'Pharma',          NOW()),
('BAJFINANCE', 'Bajaj Finance Ltd',              'NSE', 'NBFC',            NOW()),
('KOTAKBANK',  'Kotak Mahindra Bank Ltd',        'NSE', 'Banking',         NOW()),
('TITAN',      'Titan Company Ltd',              'BSE', 'Consumer',        NOW()),
('NESTLEIND',  'Nestle India Ltd',               'NSE', 'FMCG',            NOW()),
('POWERGRID',  'Power Grid Corporation',         'NSE', 'Utilities',       NOW()),
('ONGC',       'Oil and Natural Gas Corp',       'NSE', 'Energy',          NOW())
ON DUPLICATE KEY UPDATE symbol = symbol;

-- Concall Events (Q4 FY2025 results)
INSERT INTO concall_events (stock_id, concall_date, announced_at, baseline_price, quarter, fiscal_year, result_type, created_at)
SELECT s.id, '2025-04-18', '2025-04-18 14:30:00', 2850.75, 'Q4', 'FY2025', 'Quarterly', NOW()
FROM stocks s WHERE s.symbol = 'RELIANCE' AND NOT EXISTS (
  SELECT 1 FROM concall_events ce WHERE ce.stock_id = s.id AND ce.concall_date = '2025-04-18');

INSERT INTO concall_events (stock_id, concall_date, announced_at, baseline_price, quarter, fiscal_year, result_type, created_at)
SELECT s.id, '2025-04-11', '2025-04-11 15:00:00', 3620.40, 'Q4', 'FY2025', 'Quarterly', NOW()
FROM stocks s WHERE s.symbol = 'TCS' AND NOT EXISTS (
  SELECT 1 FROM concall_events ce WHERE ce.stock_id = s.id AND ce.concall_date = '2025-04-11');

INSERT INTO concall_events (stock_id, concall_date, announced_at, baseline_price, quarter, fiscal_year, result_type, created_at)
SELECT s.id, '2025-04-19', '2025-04-19 14:00:00', 1748.20, 'Q4', 'FY2025', 'Quarterly', NOW()
FROM stocks s WHERE s.symbol = 'HDFCBANK' AND NOT EXISTS (
  SELECT 1 FROM concall_events ce WHERE ce.stock_id = s.id AND ce.concall_date = '2025-04-19');

INSERT INTO concall_events (stock_id, concall_date, announced_at, baseline_price, quarter, fiscal_year, result_type, created_at)
SELECT s.id, '2025-04-17', '2025-04-17 15:30:00', 1485.60, 'Q4', 'FY2025', 'Quarterly', NOW()
FROM stocks s WHERE s.symbol = 'INFY' AND NOT EXISTS (
  SELECT 1 FROM concall_events ce WHERE ce.stock_id = s.id AND ce.concall_date = '2025-04-17');

INSERT INTO concall_events (stock_id, concall_date, announced_at, baseline_price, quarter, fiscal_year, result_type, created_at)
SELECT s.id, '2025-04-26', '2025-04-26 14:30:00', 1292.80, 'Q4', 'FY2025', 'Quarterly', NOW()
FROM stocks s WHERE s.symbol = 'ICICIBANK' AND NOT EXISTS (
  SELECT 1 FROM concall_events ce WHERE ce.stock_id = s.id AND ce.concall_date = '2025-04-26');

INSERT INTO concall_events (stock_id, concall_date, announced_at, baseline_price, quarter, fiscal_year, result_type, created_at)
SELECT s.id, '2025-04-30', '2025-04-30 14:00:00', 1876.50, 'Q4', 'FY2025', 'Quarterly', NOW()
FROM stocks s WHERE s.symbol = 'BHARTIARTL' AND NOT EXISTS (
  SELECT 1 FROM concall_events ce WHERE ce.stock_id = s.id AND ce.concall_date = '2025-04-30');

INSERT INTO concall_events (stock_id, concall_date, announced_at, baseline_price, quarter, fiscal_year, result_type, created_at)
SELECT s.id, '2025-04-16', '2025-04-16 15:00:00', 462.30, 'Q4', 'FY2025', 'Quarterly', NOW()
FROM stocks s WHERE s.symbol = 'WIPRO' AND NOT EXISTS (
  SELECT 1 FROM concall_events ce WHERE ce.stock_id = s.id AND ce.concall_date = '2025-04-16');

INSERT INTO concall_events (stock_id, concall_date, announced_at, baseline_price, quarter, fiscal_year, result_type, created_at)
SELECT s.id, '2025-05-03', '2025-05-03 14:30:00', 812.40, 'Q4', 'FY2025', 'Quarterly', NOW()
FROM stocks s WHERE s.symbol = 'SBIN' AND NOT EXISTS (
  SELECT 1 FROM concall_events ce WHERE ce.stock_id = s.id AND ce.concall_date = '2025-05-03');

INSERT INTO concall_events (stock_id, concall_date, announced_at, baseline_price, quarter, fiscal_year, result_type, created_at)
SELECT s.id, '2025-05-07', '2025-05-07 14:00:00', 3485.20, 'Q4', 'FY2025', 'Quarterly', NOW()
FROM stocks s WHERE s.symbol = 'LT' AND NOT EXISTS (
  SELECT 1 FROM concall_events ce WHERE ce.stock_id = s.id AND ce.concall_date = '2025-05-07');

INSERT INTO concall_events (stock_id, concall_date, announced_at, baseline_price, quarter, fiscal_year, result_type, created_at)
SELECT s.id, '2025-04-24', '2025-04-24 15:30:00', 1648.90, 'Q4', 'FY2025', 'Quarterly', NOW()
FROM stocks s WHERE s.symbol = 'HCLTECH' AND NOT EXISTS (
  SELECT 1 FROM concall_events ce WHERE ce.stock_id = s.id AND ce.concall_date = '2025-04-24');

INSERT INTO concall_events (stock_id, concall_date, announced_at, baseline_price, quarter, fiscal_year, result_type, created_at)
SELECT s.id, '2025-05-09', '2025-05-09 14:30:00', 986.40, 'Q4', 'FY2025', 'Quarterly', NOW()
FROM stocks s WHERE s.symbol = 'TATAMOTORS' AND NOT EXISTS (
  SELECT 1 FROM concall_events ce WHERE ce.stock_id = s.id AND ce.concall_date = '2025-05-09');

INSERT INTO concall_events (stock_id, concall_date, announced_at, baseline_price, quarter, fiscal_year, result_type, created_at)
SELECT s.id, '2025-05-06', '2025-05-06 15:00:00', 2784.60, 'Q4', 'FY2025', 'Quarterly', NOW()
FROM stocks s WHERE s.symbol = 'BAJFINANCE' AND NOT EXISTS (
  SELECT 1 FROM concall_events ce WHERE ce.stock_id = s.id AND ce.concall_date = '2025-05-06');

-- Price Snapshots for RELIANCE Q4 FY2025
INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-18 15:30:00', 2858.20, 12540000, 0.26, 1.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'RELIANCE' AND ce.concall_date = '2025-04-18'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 1.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-18 18:30:00', 2876.40, 8920000, 0.90, 4.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'RELIANCE' AND ce.concall_date = '2025-04-18'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 4.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-19 14:30:00', 2912.60, 15680000, 2.17, 24.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'RELIANCE' AND ce.concall_date = '2025-04-18'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 24.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-22 14:30:00', 2948.30, 18240000, 3.42, 48.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'RELIANCE' AND ce.concall_date = '2025-04-18'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 48.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-25 14:30:00', 2994.80, 14560000, 5.05, 120.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'RELIANCE' AND ce.concall_date = '2025-04-18'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 120.00);

-- Price Snapshots for TCS Q4 FY2025
INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-11 16:00:00', 3598.40, 4280000, -0.61, 1.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'TCS' AND ce.concall_date = '2025-04-11'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 1.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-11 19:00:00', 3572.80, 3140000, -1.32, 4.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'TCS' AND ce.concall_date = '2025-04-11'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 4.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-14 15:00:00', 3548.60, 5920000, -1.98, 24.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'TCS' AND ce.concall_date = '2025-04-11'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 24.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-15 15:00:00', 3524.20, 6840000, -2.65, 48.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'TCS' AND ce.concall_date = '2025-04-11'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 48.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-18 15:00:00', 3680.50, 7960000, 1.66, 120.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'TCS' AND ce.concall_date = '2025-04-11'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 120.00);

-- Price Snapshots for HDFCBANK
INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-19 15:00:00', 1762.40, 18420000, 0.81, 1.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'HDFCBANK' AND ce.concall_date = '2025-04-19'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 1.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-19 18:00:00', 1780.60, 12840000, 1.85, 4.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'HDFCBANK' AND ce.concall_date = '2025-04-19'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 4.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-22 14:00:00', 1798.40, 22640000, 2.87, 24.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'HDFCBANK' AND ce.concall_date = '2025-04-19'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 24.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-23 14:00:00', 1812.80, 19840000, 3.70, 48.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'HDFCBANK' AND ce.concall_date = '2025-04-19'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 48.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-26 14:00:00', 1840.20, 16920000, 5.27, 120.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'HDFCBANK' AND ce.concall_date = '2025-04-19'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 120.00);

-- Price Snapshots for INFY
INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-17 16:30:00', 1472.30, 9840000, -0.89, 1.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'INFY' AND ce.concall_date = '2025-04-17'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 1.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-17 19:30:00', 1461.80, 7240000, -1.60, 4.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'INFY' AND ce.concall_date = '2025-04-17'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 4.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-18 15:30:00', 1498.40, 12640000, 0.86, 24.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'INFY' AND ce.concall_date = '2025-04-17'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 24.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-21 15:30:00', 1518.90, 10940000, 2.24, 48.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'INFY' AND ce.concall_date = '2025-04-17'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 48.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-24 15:30:00', 1548.20, 8960000, 4.22, 120.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'INFY' AND ce.concall_date = '2025-04-17'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 120.00);

-- Price Snapshots for ICICIBANK
INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-26 15:30:00', 1308.40, 24680000, 1.21, 1.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'ICICIBANK' AND ce.concall_date = '2025-04-26'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 1.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-26 18:30:00', 1324.60, 18240000, 2.46, 4.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'ICICIBANK' AND ce.concall_date = '2025-04-26'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 4.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-29 14:30:00', 1348.20, 28640000, 4.28, 24.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'ICICIBANK' AND ce.concall_date = '2025-04-26'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 24.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-04-30 14:30:00', 1362.80, 22480000, 5.41, 48.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'ICICIBANK' AND ce.concall_date = '2025-04-26'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 48.00);

INSERT INTO price_snapshots (concall_event_id, stock_id, snapshot_time, price, volume, drift_pct, hours_after_concall, created_at)
SELECT ce.id, s.id, '2025-05-03 14:30:00', 1388.40, 19640000, 7.39, 120.00, NOW()
FROM concall_events ce JOIN stocks s ON ce.stock_id = s.id
WHERE s.symbol = 'ICICIBANK' AND ce.concall_date = '2025-04-26'
AND NOT EXISTS (SELECT 1 FROM price_snapshots ps WHERE ps.concall_event_id = ce.id AND ps.hours_after_concall = 120.00);
