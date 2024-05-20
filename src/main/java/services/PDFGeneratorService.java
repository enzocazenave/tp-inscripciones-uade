package services;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;

import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.mercadopago.MercadoPagoConfig;
import controllers.CursosController;
import controllers.DocentesController;
import controllers.MateriasController;
import impl.Curso;
import impl.Docente;
import impl.Materia;

import java.awt.*;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class PDFGeneratorService {
    private static PDFGeneratorService instance = new PDFGeneratorService();

    private PDFGeneratorService() {}

    public static PDFGeneratorService getInstance() {
        return instance;
    }

    public void generarInformeDeCursosAsignadosPorDocente(UUID legajoDocente) throws FileNotFoundException {
        CursosController cursosController = CursosController.getInstance();
        DocentesController docentesController = DocentesController.getInstance();
        MateriasController materiasController = MateriasController.getInstance();

        Docente docente = docentesController.getDocentePorLegajo(legajoDocente);
        ArrayList<UUID> cursosAsignados = docentesController.getCursosAsignadosPorLegajoDocente(legajoDocente);

        String nombrePdf = "./informes-pdf/" + docente.getNombre() + '-' + docente.getApellido() + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(nombrePdf);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Style tituloStyle = new Style().setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER);

            document.add(new Paragraph("Cursos asignados"));

            Paragraph titulo = new Paragraph(docente.getNombre() + ' ' + docente.getApellido()).addStyle(tituloStyle);
            titulo.setMargin(20);
            document.add(titulo);

            Table table = new Table(4);
            table.setWidth(UnitValue.createPercentValue(100));

            DeviceRgb headerCellBackgroundColor = this.getRandomColor();

            Style headerCellStyle = new Style().setTextAlignment(TextAlignment.CENTER).setBold().setBackgroundColor(headerCellBackgroundColor);
            Style cellStyle = new Style().setTextAlignment(TextAlignment.CENTER);

            table.addCell(new Cell().add(new Paragraph("Materia")).addStyle(headerCellStyle));
            table.addCell(new Cell().add(new Paragraph("Fecha de inicio")).addStyle(headerCellStyle));
            table.addCell(new Cell().add(new Paragraph("Día y Horario")).addStyle(headerCellStyle));
            table.addCell(new Cell().add(new Paragraph("Aula")).addStyle(headerCellStyle));

            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(new Locale("es", "ES"));

            for (UUID cursoId : cursosAsignados) {
                Curso curso = cursosController.getCursoPorId(cursoId);
                Materia materia = materiasController.getMateriaPorCodigo(curso.getCodigoMateria());

                table.addCell(new Cell().add(new Paragraph(materia.getNombre()))).addStyle(cellStyle);
                table.addCell(new Cell().add(new Paragraph(curso.getFechaInicio().format(formatter).toString()))).addStyle(cellStyle);
                table.addCell(new Cell().add(new Paragraph(curso.getDia() + " - " + curso.getTurno().getHorarioInicio() + " a " + curso.getTurno().getHorarioFinalizacion()))).addStyle(cellStyle);
                table.addCell(new Cell().add(new Paragraph(String.valueOf(curso.getNumeroAula())))).addStyle(cellStyle);
            }

            document.add(table);

            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FileNotFoundException("No se pudo generar el PDF en la ubicación especificada.");
        }
    }

    private DeviceRgb getRandomColor() {
        Random random = new Random();
        int randomChoice = random.nextInt(3);

        if (randomChoice == 1) {
            return new DeviceRgb(196, 219, 255);
        } else if (randomChoice == 2) {
            return new DeviceRgb(205, 255, 196);
        } else {
            return new DeviceRgb(255, 248, 196);
        }
    }
}
