package dev.axgr

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@RestController
class InvoiceController(private val engine: SpringTemplateEngine) {

  private val url = "http://localhost:8080/"

  @GetMapping("/invoices/{id}.pdf")
  fun invoice(@PathVariable id: UUID): ResponseEntity<ByteArray> {
    val invoice = Invoice(id, "2023001", LocalDate.now(), BigDecimal("123.45"))

//    Thymeleaf
    val context = Context()
    context.setVariable("invoice", invoice)
    val html = engine.process("invoice", context)

//    Jsoup
    val doc = Jsoup.parse(html)
    doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml)
    val xhtml = doc.html()

//    Flying Saucer
    val renderer = ITextRenderer().apply {
      setDocumentFromString(xhtml, url)
      fontResolver.addFont("fonts/IBMPlexSans-Regular.ttf", true)
      layout()
    }

    val stream = ByteArrayOutputStream(4096)
    renderer.createPDF(stream)
    val pdf = stream.toByteArray()

    val headers = HttpHeaders()
    headers.add(HttpHeaders.PRAGMA, "private")
    headers.add(HttpHeaders.CACHE_CONTROL, "private, must-revalidate")
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=${invoice.number}.pdf")

    return ResponseEntity
      .ok()
      .headers(headers)
      .contentType(MediaType.APPLICATION_PDF)
      .body(pdf)
  }


}

data class Invoice(val id: UUID, val number: String, val date: LocalDate, val amount: BigDecimal)
