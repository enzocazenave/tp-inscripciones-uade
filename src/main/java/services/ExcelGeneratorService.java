package services;

import com.itextpdf.kernel.colors.DeviceRgb;
import controllers.CursosController;
import controllers.DocentesController;
import controllers.InscripcionesController;
import controllers.MateriasController;
import impl.Curso;
import impl.Docente;
import impl.Materia;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class ExcelGeneratorService {
    private static ExcelGeneratorService instance = new ExcelGeneratorService();

    private ExcelGeneratorService() {}

    public static ExcelGeneratorService getInstance() {
        return instance;
    }

    public void generarInformeDeCursosAsignadosPorDocente(UUID legajoDocente) throws FileNotFoundException {
        CursosController cursosController = CursosController.getInstance();
        DocentesController docentesController = DocentesController.getInstance();
        MateriasController materiasController = MateriasController.getInstance();
        InscripcionesController inscripcionesController = InscripcionesController.getInstance();

        Docente docente = docentesController.getDocentePorLegajo(legajoDocente);
        ArrayList<UUID> cursosAsignados = docentesController.getCursosAsignadosPorLegajoDocente(legajoDocente);

        String nombreExcel = "./informes-excel/" + docente.getNombre() + '-' + docente.getApellido() + ".xlsx";

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Informe");

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        ((XSSFCellStyle) headerStyle).setFillForegroundColor(this.getRandomColor());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeightInPoints((short) 14);
        headerStyle.setFont(boldFont);

        CellStyle centeredStyle = workbook.createCellStyle();
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);
        centeredStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        centeredStyle.setBorderBottom(BorderStyle.THIN);
        centeredStyle.setBorderTop(BorderStyle.THIN);
        centeredStyle.setBorderLeft(BorderStyle.THIN);
        centeredStyle.setBorderRight(BorderStyle.THIN);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        Font normalFont = workbook.createFont();
        normalFont.setFontHeightInPoints((short) 14);
        centeredStyle.setFont(normalFont);

        Row rowTitle = sheet.createRow(0);
        Cell rowTitleCell = rowTitle.createCell(0);

        rowTitleCell.setCellValue("Cursos asignados de " + docente.getNombre() + " " + docente.getApellido());
        rowTitleCell.setCellStyle(headerStyle);

        Row rowHeader = sheet.createRow(1);

        Cell cellHeader1 = rowHeader.createCell(0);
        Cell cellHeader2 = rowHeader.createCell(1);
        Cell cellHeader3 = rowHeader.createCell(2);
        Cell cellHeader4 = rowHeader.createCell(3);
        Cell cellHeader5 = rowHeader.createCell(4);

        cellHeader1.setCellValue("Materia");
        cellHeader2.setCellValue("Fecha de inicio");
        cellHeader3.setCellValue("Día y Horario");
        cellHeader4.setCellValue("Inscriptos");
        cellHeader5.setCellValue("Aula");

        cellHeader1.setCellStyle(headerStyle);
        cellHeader2.setCellStyle(headerStyle);
        cellHeader3.setCellStyle(headerStyle);
        cellHeader4.setCellStyle(headerStyle);
        cellHeader5.setCellStyle(headerStyle);

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(new Locale("es", "ES"));
        int filaContador = 2;

        for (UUID cursoId : cursosAsignados) {
            Curso curso = cursosController.getCursoPorId(cursoId);
            Materia materia = materiasController.getMateriaPorCodigo(curso.getCodigoMateria());

            Row row = sheet.createRow(filaContador);

            Cell cellRow1 = row.createCell(0);
            Cell cellRow2 = row.createCell(1);
            Cell cellRow3 = row.createCell(2);
            Cell cellRow4 = row.createCell(3);
            Cell cellRow5 = row.createCell(4);

            cellRow1.setCellValue(materia.getNombre());
            cellRow2.setCellValue(curso.getFechaInicio().format(formatter));
            cellRow3.setCellValue(curso.getDia() + " - " + curso.getTurno().getHorarioInicio() + " a " + curso.getTurno().getHorarioFinalizacion());
            cellRow4.setCellValue(String.valueOf(inscripcionesController.getCantidadAlumnosEnCurso(cursoId)));
            cellRow5.setCellValue(String.valueOf(curso.getNumeroAula()));

            cellRow1.setCellStyle(centeredStyle);
            cellRow2.setCellStyle(centeredStyle);
            cellRow3.setCellStyle(centeredStyle);
            cellRow4.setCellStyle(centeredStyle);
            cellRow5.setCellStyle(centeredStyle);

            filaContador++;
        }

        for (int i = 0; i < sheet.getRow(1).getLastCellNum(); i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(nombreExcel)) {
            workbook.write(fos);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileNotFoundException("No se pudo generar el Excel en la ubicación especificada.");
        }
    }

    public XSSFColor getRandomColor() {
        Random random = new Random();
        int randomChoice = random.nextInt(3);

        if (randomChoice == 1) {
            return new XSSFColor(new byte[]{(byte) 196, (byte) 219, (byte) 255}, null);
        } else if (randomChoice == 2) {
            return new XSSFColor(new byte[]{(byte) 205, (byte) 255, (byte) 196}, null);
        } else {
            return new XSSFColor(new byte[]{(byte) 255, (byte) 248, (byte) 196}, null);

        }
    }
}
