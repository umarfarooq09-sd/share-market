package com.concalldrift.controller;

import com.concalldrift.dto.ApiResponse;
import com.concalldrift.dto.DriftBoardRow;
import com.concalldrift.scheduler.PriceUpdateScheduler;
import com.concalldrift.service.DriftService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/drift")
@RequiredArgsConstructor
public class DriftController {

    private final DriftService driftService;
    private final PriceUpdateScheduler priceUpdateScheduler;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refreshPrices() {
        new Thread(priceUpdateScheduler::run).start();
        return ResponseEntity.ok(ApiResponse.ok("Price refresh triggered — check logs for progress"));
    }

    @GetMapping("/board")
    public ResponseEntity<ApiResponse<List<DriftBoardRow>>> getDriftBoard(
            @RequestParam(required = false) String exchange,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<DriftBoardRow> rows = driftService.getDriftBoard(exchange, fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.ok(rows));
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(required = false) String exchange,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<DriftBoardRow> rows = driftService.getDriftBoard(exchange, fromDate, toDate);
        String csv = buildCsv(rows);
        byte[] bytes = csv.getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "concall-drift-export.csv");
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    private String buildCsv(List<DriftBoardRow> rows) {
        StringBuilder sb = new StringBuilder();
        sb.append("Symbol,Company,Exchange,Sector,Quarter,FY,Result Type,Concall Date,Baseline Price," +
                  "1H Drift%,4H Drift%,1D Drift%,2D Drift%,5D Drift%,Current Price,Current Drift%,Last Updated\n");
        for (DriftBoardRow r : rows) {
            sb.append(csv(r.getSymbol())).append(",")
              .append(csv(r.getCompanyName())).append(",")
              .append(csv(r.getExchange())).append(",")
              .append(csv(r.getSector())).append(",")
              .append(csv(r.getQuarter())).append(",")
              .append(csv(r.getFiscalYear())).append(",")
              .append(csv(r.getResultType())).append(",")
              .append(r.getConcallDate()).append(",")
              .append(r.getBaselinePrice()).append(",")
              .append(r.getDrift1h()).append(",")
              .append(r.getDrift4h()).append(",")
              .append(r.getDrift1d()).append(",")
              .append(r.getDrift2d()).append(",")
              .append(r.getDrift5d()).append(",")
              .append(r.getCurrentPrice()).append(",")
              .append(r.getDriftCurrent()).append(",")
              .append(r.getLastUpdated()).append("\n");
        }
        return sb.toString();
    }

    private String csv(String val) {
        if (val == null) return "";
        if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }
}
