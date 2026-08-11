package airlineReservation.test.support;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * JUnit テスト実行結果を Excel ファイルへ出力するリスナー。
 * テスト実行後、build/test-results/booking-test-results.xlsx が生成される。
 */
public class TestResultExcelExporter implements TestExecutionListener {

    private static final List<TestResultRow> RESULTS = new CopyOnWriteArrayList<>();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LocalDateTime startedAt;

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        RESULTS.clear();
        startedAt = LocalDateTime.now();
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (!testIdentifier.isTest()) {
            return;
        }

        TestExecutionResult.Status status = testExecutionResult.getStatus();
        String statusLabel = switch (status) {
            case SUCCESSFUL -> "成功";
            case FAILED -> "失敗";
            case ABORTED -> "中断";
        };

        String errorMessage = testExecutionResult.getThrowable()
                .map(Throwable::getMessage)
                .orElse("");

        RESULTS.add(new TestResultRow(
                testIdentifier.getDisplayName(),
                testIdentifier.getLegacyReportingName(),
                statusLabel,
                errorMessage,
                LocalDateTime.now()
        ));
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        exportToExcel();
    }

    private void exportToExcel() {
        Path outputDir = Path.of("build", "test-results");
        Path outputFile = outputDir.resolve("booking-test-results.xlsx");

        try {
            Files.createDirectories(outputDir);

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet summarySheet = workbook.createSheet("要約");
                Sheet detailSheet = workbook.createSheet("テスト詳細");

                long successCount = RESULTS.stream().filter(r -> "成功".equals(r.status())).count();
                long failCount = RESULTS.stream().filter(r -> "失敗".equals(r.status())).count();
                long abortCount = RESULTS.stream().filter(r -> "中断".equals(r.status())).count();

                writeSummarySheet(summarySheet, workbook, successCount, failCount, abortCount);
                writeDetailSheet(detailSheet, workbook);

                try (OutputStream outputStream = Files.newOutputStream(outputFile)) {
                    workbook.write(outputStream);
                }
            }

            System.out.println("[TestResultExcelExporter] テスト結果 Excel 生成完了: " + outputFile.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("[TestResultExcelExporter] Excel 生成失敗: " + e.getMessage());
        }
    }

    private void writeSummarySheet(Sheet sheet, Workbook workbook, long successCount, long failCount, long abortCount) {
        CellStyle headerStyle = createHeaderStyle(workbook);

        Row headerRow = sheet.createRow(0);
        createCell(headerRow, 0, "項目", headerStyle);
        createCell(headerRow, 1, "値", headerStyle);

        List<String[]> summaryRows = new ArrayList<>();
        summaryRows.add(new String[]{"実行日時", startedAt.format(DATE_TIME_FORMATTER)});
        summaryRows.add(new String[]{"完了日時", LocalDateTime.now().format(DATE_TIME_FORMATTER)});
        summaryRows.add(new String[]{"総テスト数", String.valueOf(RESULTS.size())});
        summaryRows.add(new String[]{"成功", String.valueOf(successCount)});
        summaryRows.add(new String[]{"失敗", String.valueOf(failCount)});
        summaryRows.add(new String[]{"中断", String.valueOf(abortCount)});
        summaryRows.add(new String[]{"成功率", RESULTS.isEmpty() ? "0%" : String.format("%.1f%%", successCount * 100.0 / RESULTS.size())});

        for (int i = 0; i < summaryRows.size(); i++) {
            Row row = sheet.createRow(i + 1);
            createCell(row, 0, summaryRows.get(i)[0], null);
            createCell(row, 1, summaryRows.get(i)[1], null);
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private void writeDetailSheet(Sheet sheet, Workbook workbook) {
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle failStyle = workbook.createCellStyle();
        failStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        failStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"番号", "テスト名", "クラス.メソッド", "結果", "エラーメッセージ", "実行時刻"};
        for (int i = 0; i < headers.length; i++) {
            createCell(headerRow, i, headers[i], headerStyle);
        }

        for (int i = 0; i < RESULTS.size(); i++) {
            TestResultRow result = RESULTS.get(i);
            Row row = sheet.createRow(i + 1);
            CellStyle rowStyle = "失敗".equals(result.status()) ? failStyle : null;

            createCell(row, 0, String.valueOf(i + 1), rowStyle);
            createCell(row, 1, result.displayName(), rowStyle);
            createCell(row, 2, result.legacyName(), rowStyle);
            createCell(row, 3, result.status(), rowStyle);
            createCell(row, 4, result.errorMessage(), rowStyle);
            createCell(row, 5, result.executedAt().format(DATE_TIME_FORMATTER), rowStyle);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private void createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    private record TestResultRow(
            String displayName,
            String legacyName,
            String status,
            String errorMessage,
            LocalDateTime executedAt
    ) {
    }
}
