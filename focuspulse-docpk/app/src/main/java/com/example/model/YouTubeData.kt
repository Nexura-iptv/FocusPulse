package com.example.model

data class YouTubeChannel(
    val name: String,
    val handle: String,
    val url: String,
    val description: String,
    val subjectFocus: String,
    val defaultVideoId: String? = null
)

object YouTubeDirectory {
    val quickAccessChannels = listOf(
        YouTubeChannel(
            name = "Hocalara Geldik",
            handle = "@hocalarageldik",
            url = "https://www.youtube.com/@hocalarageldik",
            description = "Lise, YKS ve ortaokul tüm branş konu anlatımları ve soru çözümleri.",
            subjectFocus = "Tüm Branşlar & YKS",
            defaultVideoId = "dQw4w9WgXcQ" // Fallback / placeholder
        ),
        YouTubeChannel(
            name = "Benim Hocam",
            handle = "@BenimHocam",
            url = "https://www.youtube.com/@BenimHocam",
            description = "KPSS, YKS Tarih, Coğrafya, Türkçe, Vatandaşlık ve Eğitim Bilimleri.",
            subjectFocus = "KPSS & Sözel Branşlar",
            defaultVideoId = null
        ),
        YouTubeChannel(
            name = "Rehber Matematik",
            handle = "@RehberMatematik",
            url = "https://www.youtube.com/@RehberMatematik",
            description = "TYT & AYT Matematik kampları, geometri ve 'Gönder Gelsin' serileri.",
            subjectFocus = "TYT - AYT Matematik",
            defaultVideoId = null
        ),
        YouTubeChannel(
            name = "Tonguç Akademi",
            handle = "@tongucakademi",
            url = "https://www.youtube.com/@tongucakademi",
            description = "İlkokul, LGS (8. Sınıf), ortaokul ve lise eğlenceli ders içerikleri.",
            subjectFocus = "1-8. Sınıf & LGS",
            defaultVideoId = null
        ),
        YouTubeChannel(
            name = "Bıyıklı Matematik",
            handle = "@biyiklimatematik",
            url = "https://www.youtube.com/@biyiklimatematik",
            description = "Temelden zirveye TYT ve AYT Matematik konu anlatımları.",
            subjectFocus = "YKS - KPSS Matematik",
            defaultVideoId = null
        )
    )

    fun extractVideoId(input: String): String? {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return null

        // Pure 11-character video ID
        if (trimmed.length == 11 && trimmed.matches(Regex("^[a-zA-Z0-9_-]{11}$"))) {
            return trimmed
        }

        // Standard watch URL: youtube.com/watch?v=VIDEO_ID
        val watchRegex = Regex("(?:v=|/v/|youtu\\.be/|/embed/|/live/|/shorts/)([a-zA-Z0-9_-]{11})")
        val match = watchRegex.find(trimmed)
        if (match != null) {
            return match.groupValues[1]
        }

        return null
    }

    fun buildIframeHtml(videoId: String): String {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    body, html {
                        background-color: #020617;
                        width: 100%;
                        height: 100%;
                        overflow: hidden;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                    }
                    .video-container {
                        position: relative;
                        width: 100%;
                        height: 100%;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                    }
                    iframe {
                        width: 100%;
                        height: 100%;
                        border: none;
                    }
                </style>
            </head>
            <body>
                <div class="video-container">
                    <iframe 
                        src="https://www.youtube-nocookie.com/embed/$videoId?autoplay=1&playsinline=1&rel=0&modestbranding=1" 
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
                        allowfullscreen>
                    </iframe>
                </div>
            </body>
            </html>
        """.trimIndent()
    }
}
