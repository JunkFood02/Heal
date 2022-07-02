package io.github.junkfood.heal.ui.component

import android.graphics.Typeface
import android.text.Html
import android.text.Spanned
import android.text.style.*
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.core.text.getSpans

private const val TAG = "HtmlText"
lateinit var annotations: List<AnnotatedString.Range<String>>

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    text: String,
    urlSpanStyle: SpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.Underline
    ),
    isTimeStampEnabled: Boolean = true,
    timeStampSpanStyle: SpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace
    ),
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    val annotatedString =
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
            .toAnnotatedString(urlSpanStyle, timeStampSpanStyle)
    annotations = annotatedString.getStringAnnotations(0, annotatedString.length)
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    Text(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = { pos ->
                    layoutResult.value?.let { layoutResult ->
                        val position = layoutResult.getOffsetForPosition(pos)
                        annotations.forEach { sa ->
                            if (position >= sa.start && position <= sa.end) {
                                if (sa.tag == "url") { // NON-NLS
                                    uriHandler.openUri(sa.item)
                                } else if (sa.tag == "timeStamp") {
                                    Toast.makeText(context, sa.item, Toast.LENGTH_SHORT).show()
                                }
                                return@detectTapGestures
                            }
                        }
                    }
                })
            },
        text = annotatedString,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        inlineContent = inlineContent,
        onTextLayout = {
            layoutResult.value = it
            onTextLayout(it)
        },
        style = style
    )
}


fun Spanned.toAnnotatedString(
    urlSpanStyle: SpanStyle = SpanStyle(
        color = Color.Blue,
        textDecoration = TextDecoration.Underline
    ),
    timeStampSpanStyle: SpanStyle = SpanStyle(
        color = Color.Blue, fontWeight = FontWeight.Bold
    ),
    isTimeStampEnabled: Boolean = true

): AnnotatedString {
    return buildAnnotatedString {
        val rawString = this@toAnnotatedString.toString()
        append(rawString)
        val urlSpans = getSpans<URLSpan>()
        val styleSpans = getSpans<StyleSpan>()
        val colorSpans = getSpans<ForegroundColorSpan>()
        val underlineSpans = getSpans<UnderlineSpan>()
        val strikethroughSpans = getSpans<StrikethroughSpan>()
        if (isTimeStampEnabled) {
            Regex("(\\d{1,2}:)+\\d\\d").findAll(rawString).forEach {
                addStyle(
                    timeStampSpanStyle, it.range.first,
                    it.range.last + 1
                )
                addStringAnnotation(
                    "timeStamp",
                    rawString.substring(it.range),
                    it.range.first,
                    it.range.last + 1
                )
            }
        }
        urlSpans.forEach { urlSpan ->
            val start = getSpanStart(urlSpan)
            val end = getSpanEnd(urlSpan)
            addStyle(urlSpanStyle, start, end)
            addStringAnnotation("url", urlSpan.url, start, end) // NON-NLS
        }
        colorSpans.forEach { colorSpan ->
            val start = getSpanStart(colorSpan)
            val end = getSpanEnd(colorSpan)
            addStyle(SpanStyle(color = Color(colorSpan.foregroundColor)), start, end)
        }
        styleSpans.forEach { styleSpan ->
            val start = getSpanStart(styleSpan)
            val end = getSpanEnd(styleSpan)
            when (styleSpan.style) {
                Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                Typeface.BOLD_ITALIC -> addStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ), start, end
                )
            }
        }
        underlineSpans.forEach { underlineSpan ->
            val start = getSpanStart(underlineSpan)
            val end = getSpanEnd(underlineSpan)
            addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
        }
        strikethroughSpans.forEach { strikethroughSpan ->
            val start = getSpanStart(strikethroughSpan)
            val end = getSpanEnd(strikethroughSpan)
            addStyle(SpanStyle(textDecoration = TextDecoration.LineThrough), start, end)
        }
    }
}