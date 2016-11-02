package pdfUtilities;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
 
public class PDFCreator {
	
	private static Paragraph newLine = new Paragraph(" ");
	
	private static String logoSource="";
	
	private static String cif="";
	private static String address="";
	private static String postalCode="";
	private static String province="";
	private static String phoneNumber="";
	private static String emailAddress="";
	private static String webAddress="";
	
	private static String clientName="";
	private static String clientCif="";
	private static String clientAddress="";
	private static String clientPostalCode="";
	private static String clientProvince="";
	
	private static String billCode="";
	private static String date="";
	
	private static ArrayList<String> descriptions = new ArrayList<String>();
	private static ArrayList<String> quantities = new ArrayList<String>();
	private static ArrayList<String> prizes = new ArrayList<String>();
	private static ArrayList<String> totallies = new ArrayList<String>();
	private static ArrayList<String> ivaTypes = new ArrayList<String>();

	private static String billTotalWithoutIva="";
	private static String ivaQuantity="";
	private static String billTotal="";
	private static String payMethod="";
	
	private static String observations="";
	private static String footer="";
	
	/**
	 * 
	 * @param destinationPath
	 * @throws DocumentException: lanzada por Document (non se lle poden añadir elementos, non se creou...)
	 * @throws IOException: Lanzada polos elementos que intentan acceder a unha ruta (Image, BaseFont, FileOutPutStream..
	 */
	public static void crearPDF (String destinationPath) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4, 50, 50, 54, 0);
	   
		FileOutputStream fileOutputStream = new FileOutputStream(destinationPath);

		PdfWriter pdfWriter = PdfWriter.getInstance(document, fileOutputStream);

		BaseFont baseFont = BaseFont.createFont("/fonts/CenturyGothic.ttf", BaseFont.CP1252, 
							BaseFont.EMBEDDED);
		Font font = new Font (baseFont, 10f, Font.NORMAL);
		Font boldFont = new Font (baseFont, 10f, Font.BOLD);
		
		document.open();
		
		Image logoImage = null;
		try {
			if(logoSource == null || logoSource.isEmpty())
				throw new IOException();
			else
				logoImage = Image.getInstance(logoSource);
		} catch (BadElementException | IOException e) {
		}
		
		if (logoImage != null) {
			logoImage.scaleAbsolute(160, 85);
			document.add(logoImage);
		}
		
        document.add(newLine);			
		
        PdfPTable headers = createHeaders(font, boldFont);
        document.add(headers);
        
		PdfContentByte contentByte = pdfWriter.getDirectContent();
		
		contentByte.setFontAndSize(baseFont, 10);
        contentByte.beginText();

		contentByte.setTextMatrix(180, 570); //position fixed
		contentByte.showText("Factura Nº");
		contentByte.setTextMatrix(195, 555);
		contentByte.showText(billCode);
		
		contentByte.setTextMatrix(340, 570);
		contentByte.showText("Fecha");
		contentByte.setTextMatrix(340, 555);
		contentByte.showText(date);
		
		contentByte.endText();
        
		document.add(newLine);
		document.add(newLine);
		document.add(newLine);
		document.add(newLine);
			
		PdfPTable details = createDetails(font, boldFont);
		document.add(details);
		
		PdfPTable summary = createSummary(font, boldFont);
		document.add(summary);
		
		document.add(newLine);
		document.add(newLine);
		document.add(newLine);
		document.add(newLine);

		PdfPTable footerTable = new PdfPTable(1);
		footerTable.setWidthPercentage(100);
				
		font.setSize(6f);
		PdfPCell cell = new PdfPCell(new Phrase(footer, font));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthTop(3);
		cell.setPaddingTop(5);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);

		footerTable.addCell(cell);

		document.add(footerTable);

		document.close();
	}
	
	/****************************
	 * Añade o documento os datos da empresa e do cliente
	 * @throws DocumentException 
	 * 
	 ****************************/
	private static PdfPTable createHeaders (Font font, Font boldFont) throws DocumentException {
		float leading = (float) 16;
        
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
        float[] colWidths = { 45, 55 };
        table.setWidths(colWidths);
        
        PdfPCell fiscalCell = new PdfPCell();
        fiscalCell.setBorder(Rectangle.NO_BORDER);
        
        font.setSize(8f);
        
		Paragraph fiscalParagraph1 = new Paragraph(new Chunk("CIF " + cif, font));
		fiscalCell.addElement(fiscalParagraph1);
					
		Paragraph fiscalParagraph2 = new Paragraph(leading, new Chunk(address, font));
		fiscalCell.addElement(fiscalParagraph2);
		
		Paragraph fiscalParagraph3 = new Paragraph(leading, new Chunk(postalCode + " " + province, font));
		fiscalCell.addElement(fiscalParagraph3);
		
		Paragraph fiscalParagraph4 = new Paragraph(leading, new Chunk("Tel/Fax " + phoneNumber, font));
		fiscalCell.addElement(fiscalParagraph4);
		
		Paragraph fiscalParagraph5 = new Paragraph(leading, new Chunk(emailAddress, font));
		fiscalCell.addElement(fiscalParagraph5);
		
		Paragraph fiscalParagraph6 = new Paragraph(leading, new Chunk(webAddress, font));
		fiscalCell.addElement(fiscalParagraph6);
		
		table.addCell(fiscalCell);
		
		PdfPCell clientCell = new PdfPCell();
        clientCell.setBorder(Rectangle.NO_BORDER);
        
        font.setSize(10f);
        
        Paragraph clientParagraph1 = new Paragraph(leading, new Chunk(clientName, font));
		clientCell.addElement(clientParagraph1);
		
        Paragraph clientParagraph2 = new Paragraph(leading, new Chunk(clientCif, font));
		clientCell.addElement(clientParagraph2);

		Paragraph clientParagraph3 = new Paragraph(leading, new Chunk(clientAddress, font));
		clientCell.addElement(clientParagraph3);

        Paragraph clientParagraph4 = new Paragraph(leading, new Chunk(clientPostalCode + " " + clientProvince, font));
		clientCell.addElement(clientParagraph4);

        table.addCell(clientCell);

        return table;
	}
	
	/**
	 * Añade ó documento as líneas da factura
	 * @param documento
	 * @throws DocumentException 
	 */
	private static PdfPTable createDetails (Font font, Font boldFont) throws DocumentException {
		float[] detailsTableWidths = {60, 10, 10, 10, 10};
		PdfPTable detailsTable = new PdfPTable(detailsTableWidths);
		detailsTable.setWidthPercentage(100);
		detailsTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		detailsTable.getDefaultCell().setBorderWidthBottom((float)1.5);
		
		PdfPCell cell = new PdfPCell(new Phrase("Descripcion", boldFont));
		cell.setPaddingBottom(5);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		detailsTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Cant", boldFont));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		detailsTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Precio", boldFont));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		detailsTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Total", boldFont));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		detailsTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Tipo IVA", boldFont));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		detailsTable.addCell(cell);
		

		detailsTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		
		if (descriptions.size() == quantities.size() && descriptions.size() == prizes.size() 
				&& descriptions.size() == totallies.size() && descriptions.size() == ivaTypes.size() && !descriptions.isEmpty()) {
			for(int index = 0; index < descriptions.size(); index++) {
				cell = new PdfPCell(new Phrase(descriptions.get(index), font));
				cell.setBorder(Rectangle.NO_BORDER);
				detailsTable.addCell(cell);
				
				cell = new PdfPCell(new Phrase(quantities.get(index), font));
				cell.setBorder(Rectangle.NO_BORDER);
				detailsTable.addCell(cell);
				
				cell = new PdfPCell(new Phrase(prizes.get(index), font));
				cell.setBorder(Rectangle.NO_BORDER);
				detailsTable.addCell(cell);
				
				cell = new PdfPCell(new Phrase(totallies.get(index), font));
				cell.setBorder(Rectangle.NO_BORDER);
				detailsTable.addCell(cell);
				
				cell = new PdfPCell(new Phrase(ivaTypes.get(index), font));
				cell.setBorder(Rectangle.NO_BORDER);
				detailsTable.addCell(cell);
			}
		}
		
		for (int index = 0; index < 12; index++) {
			detailsTable.addCell(newLine);
			detailsTable.completeRow();
		}
		
		detailsTable.getDefaultCell().setBorder(Rectangle.BOTTOM);
		detailsTable.addCell(newLine);
		detailsTable.completeRow();
		
		return detailsTable;
	}
	
	/**
	 * Esta función só sirve cando hai un único tipo de IVA, cando temos varios habería que crear outra. 
	 * Crea unha táboa co desglose de IVA, etc...
	 * 
	 * @throws DocumentException 
	 */
	private static PdfPTable createSummary (Font font, Font bodlFont) {
		float[] resumeTableWidths = {30, 20, 20, 10, 20};
		PdfPTable resumeTable = new PdfPTable(resumeTableWidths);
		resumeTable.setWidthPercentage(80);
		resumeTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
		resumeTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		resumeTable.getDefaultCell().setPaddingBottom(5);
		resumeTable.getDefaultCell().setBorderWidthBottom((float)1.5);
		
		PdfPCell cell = new PdfPCell(new Phrase("Items Total (sin IVA)", font));
		cell.setPaddingTop(5);
		cell.setPaddingBottom(5);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		resumeTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", font));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		resumeTable.addCell(cell);
		
		resumeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(new Phrase("en Moneda", font));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		resumeTable.addCell(cell);
		
		resumeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		cell = new PdfPCell(new Phrase("EUR", font));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		resumeTable.addCell(cell);
		
		resumeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(new Phrase(billTotalWithoutIva, font));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		resumeTable.addCell(cell);
		
		resumeTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		
		resumeTable.addCell(newLine);
		resumeTable.completeRow();
		
		resumeTable.addCell("");
		
		cell = new PdfPCell(new Phrase("Tipo IVA", font));
		cell.setBorder(Rectangle.NO_BORDER);
		resumeTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Base Imp", font));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		resumeTable.addCell(cell);
		
		resumeTable.completeRow();
		
		cell = new PdfPCell(new Phrase("IVA", font));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingBottom(5);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		resumeTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(ivaTypes.get(0) + "%", font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		resumeTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(billTotalWithoutIva, font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		resumeTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", font));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		resumeTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase(ivaQuantity, font)); //Para varios iva deberíamos meter unha línea por cada iva distinto
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom((float)1.5);
		resumeTable.addCell(cell);
		resumeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

		resumeTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

		//Fila
		cell = new PdfPCell(new Phrase("Total (IVA incluído)", bodlFont));
		cell.setBorder(Rectangle.NO_BORDER);
		resumeTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", bodlFont));
		cell.setBorder(Rectangle.NO_BORDER);
		resumeTable.addCell(cell);
		
		resumeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(new Phrase("en Moneda", bodlFont));
		cell.setBorder(Rectangle.NO_BORDER);
		resumeTable.addCell(cell);
		
		resumeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		cell = new PdfPCell(new Phrase("EUR", bodlFont));
		cell.setBorder(Rectangle.NO_BORDER);
		resumeTable.addCell(cell);
		
		resumeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(new Phrase(billTotal, bodlFont));
		cell.setBorder(Rectangle.NO_BORDER);
		resumeTable.addCell(cell);
		
		resumeTable.addCell(newLine);
		resumeTable.completeRow();
		
		PdfPCell celda2 = new PdfPCell(new Phrase("                                Forma de Pago", font));
		celda2.setBorder(Rectangle.NO_BORDER);
		celda2.setColspan(2);
		resumeTable.addCell(celda2);
		
		resumeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		cell = new PdfPCell(new Phrase(payMethod, font));
		cell.setBorder(Rectangle.NO_BORDER);
		resumeTable.addCell(cell);
		resumeTable.completeRow();
		
		resumeTable.addCell(newLine);
		resumeTable.completeRow();
		
		cell = new PdfPCell(new Phrase("Observaciones", font));
		cell.setBorder(Rectangle.NO_BORDER);
		resumeTable.addCell(cell);
		resumeTable.completeRow();
		
		cell = new PdfPCell(new Phrase(observations, font));
		cell.setBorder(Rectangle.NO_BORDER);
		resumeTable.addCell(cell);
		resumeTable.completeRow();
		
		return resumeTable;
	}

	public static Paragraph getNewLine() {
		return newLine;
	}

	public static void setNewLine(Paragraph newLine) {
		PDFCreator.newLine = newLine;
	}

	public static String getLogoSource() {
		return logoSource;
	}

	public static void setLogoSource(String logoSource) {
		PDFCreator.logoSource = logoSource;
	}

	public static String getCif() {
		return cif;
	}

	public static void setCif(String cif) {
		PDFCreator.cif = cif;
	}

	public static String getAddress() {
		return address;
	}

	public static void setAddress(String address) {
		PDFCreator.address = address;
	}

	public static String getPostalCode() {
		return postalCode;
	}

	public static void setPostalCode(String postalCode) {
		PDFCreator.postalCode = postalCode;
	}

	public static String getProvince() {
		return province;
	}

	public static void setProvince(String province) {
		PDFCreator.province = province;
	}

	public static String getPhoneNumber() {
		return phoneNumber;
	}

	public static void setPhoneNumber(String phoneNumber) {
		PDFCreator.phoneNumber = phoneNumber;
	}

	public static String getEmailAddress() {
		return emailAddress;
	}

	public static void setEmailAddress(String emailAddress) {
		PDFCreator.emailAddress = emailAddress;
	}

	public static String getWebAddress() {
		return webAddress;
	}

	public static void setWebAddress(String webAddress) {
		PDFCreator.webAddress = webAddress;
	}

	public static String getClientName() {
		return clientName;
	}

	public static void setClientName(String clientName) {
		PDFCreator.clientName = clientName;
	}

	public static String getClientCif() {
		return clientCif;
	}

	public static void setClientCif(String clientCif) {
		PDFCreator.clientCif = clientCif;
	}

	public static String getClientAddress() {
		return clientAddress;
	}

	public static void setClientAddress(String clientAddress) {
		PDFCreator.clientAddress = clientAddress;
	}

	public static String getClientPostalCode() {
		return clientPostalCode;
	}

	public static void setClientPostalCode(String clientPostalCode) {
		PDFCreator.clientPostalCode = clientPostalCode;
	}

	public static String getClientProvince() {
		return clientProvince;
	}

	public static void setClientProvince(String clientProvince) {
		PDFCreator.clientProvince = clientProvince;
	}

	public static String getBillCode() {
		return billCode;
	}

	public static void setBillCode(String billCode) {
		PDFCreator.billCode = billCode;
	}

	public static String getDate() {
		return date;
	}

	public static void setDate(String date) {
		PDFCreator.date = date;
	}

	public static ArrayList<String> getDescriptions() {
		return descriptions;
	}

	public static void setDescriptions(ArrayList<String> descriptions) {
		PDFCreator.descriptions = descriptions;
	}

	public static ArrayList<String> getQuantities() {
		return quantities;
	}

	public static void setQuantities(ArrayList<String> quantities) {
		PDFCreator.quantities = quantities;
	}

	public static ArrayList<String> getPrizes() {
		return prizes;
	}

	public static void setPrizes(ArrayList<String> prizes) {
		PDFCreator.prizes = prizes;
	}

	public static ArrayList<String> getTotallies() {
		return totallies;
	}

	public static void setTotallies(ArrayList<String> totallies) {
		PDFCreator.totallies = totallies;
	}

	public static ArrayList<String> getIvaTypes() {
		return ivaTypes;
	}

	public static void setIvaTypes(ArrayList<String> ivaTypes) {
		PDFCreator.ivaTypes = ivaTypes;
	}

	public static String getBillTotalWithoutIva() {
		return billTotalWithoutIva;
	}

	public static void setBillTotalWithoutIva(String billTotalWithoutIva) {
		PDFCreator.billTotalWithoutIva = billTotalWithoutIva;
	}

	public static String getIvaQuantity() {
		return ivaQuantity;
	}

	public static void setIvaQuantity(String ivaQuantity) {
		PDFCreator.ivaQuantity = ivaQuantity;
	}

	public static String getBillTotal() {
		return billTotal;
	}

	public static void setBillTotal(String billTotal) {
		PDFCreator.billTotal = billTotal;
	}

	public static String getPayMethod() {
		return payMethod;
	}

	public static void setPayMethod(String payMethod) {
		PDFCreator.payMethod = payMethod;
	}

	public static String getObservations() {
		return observations;
	}

	public static void setObservations(String observations) {
		PDFCreator.observations = observations;
	}

	public static String getFooter() {
		return footer;
	}

	public static void setFooter(String footer) {
		PDFCreator.footer = footer;
	}

}