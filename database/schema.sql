-- ================================================================
-- Post-Concall Announcement Drift — MySQL Schema
-- Run this to create the database and tables from scratch
-- ================================================================

CREATE DATABASE IF NOT EXISTS concall_drift
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE concall_drift;

-- Users
CREATE TABLE IF NOT EXISTS users (
  id          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username    VARCHAR(50)  NOT NULL UNIQUE,
  password    VARCHAR(255) NOT NULL,
  role        VARCHAR(20)  NOT NULL DEFAULT 'USER',
  enabled     TINYINT(1)   NOT NULL DEFAULT 1,
  created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Stocks
CREATE TABLE IF NOT EXISTS stocks (
  id           BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
  symbol       VARCHAR(20) NOT NULL UNIQUE,
  company_name VARCHAR(150) NOT NULL,
  exchange     VARCHAR(5)  NOT NULL COMMENT 'NSE or BSE',
  sector       VARCHAR(50),
  created_at   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_exchange (exchange),
  INDEX idx_sector   (sector)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Earnings concall events
CREATE TABLE IF NOT EXISTS concall_events (
  id             BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  stock_id       BIGINT         NOT NULL,
  concall_date   DATE           NOT NULL,
  announced_at   DATETIME       NOT NULL COMMENT 'Exact announcement timestamp for baseline',
  baseline_price DECIMAL(10,2)  NOT NULL COMMENT 'Stock price at announcement time',
  quarter        VARCHAR(10)    COMMENT 'e.g. Q4',
  fiscal_year    VARCHAR(10)    COMMENT 'e.g. FY2025',
  result_type    VARCHAR(20)    COMMENT 'Quarterly / Annual',
  created_at     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_ce_stock FOREIGN KEY (stock_id) REFERENCES stocks(id) ON DELETE CASCADE,
  INDEX idx_ce_stock   (stock_id),
  INDEX idx_ce_date    (concall_date),
  INDEX idx_ce_quarter (quarter, fiscal_year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Price snapshots captured after each concall
CREATE TABLE IF NOT EXISTS price_snapshots (
  id                   BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  concall_event_id     BIGINT         NOT NULL,
  stock_id             BIGINT         NOT NULL,
  snapshot_time        DATETIME       NOT NULL,
  price                DECIMAL(10,2)  NOT NULL,
  volume               BIGINT         NOT NULL DEFAULT 0,
  drift_pct            DECIMAL(8,4)   COMMENT 'Percentage drift from baseline',
  hours_after_concall  DECIMAL(8,2)   COMMENT 'Hours elapsed since announced_at',
  created_at           DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_ps_event FOREIGN KEY (concall_event_id) REFERENCES concall_events(id) ON DELETE CASCADE,
  CONSTRAINT fk_ps_stock FOREIGN KEY (stock_id)         REFERENCES stocks(id)         ON DELETE CASCADE,
  INDEX idx_ps_event     (concall_event_id),
  INDEX idx_ps_hours     (concall_event_id, hours_after_concall),
  INDEX idx_ps_time      (snapshot_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
