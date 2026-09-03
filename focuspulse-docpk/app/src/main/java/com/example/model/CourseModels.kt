package com.example.model

enum class SubjectBranch(val displayName: String) {
    ALL("Tüm Branşlar"),
    MATHEMATICS("Matematik"),
    TURKISH("Türkçe"),
    SCIENCE("Fen"),
    SOFTWARE("Yazılım"),
    SOCIAL("Sosyal")
}

data class GradeLevel(
    val id: String,
    val title: String,
    val category: String // İlkokul, Ortaokul, Lise, Üniversiteye Hazırlık, KPSS
)

data class CourseItem(
    val id: String,
    val title: String,
    val gradeId: String,
    val branch: SubjectBranch,
    val summary: String,
    val recommendedChannel: String,
    val topics: List<String>
)

object CourseRepository {
    val gradeLevels = listOf(
        GradeLevel("1", "1. Sınıf", "İlkokul"),
        GradeLevel("2", "2. Sınıf", "İlkokul"),
        GradeLevel("3", "3. Sınıf", "İlkokul"),
        GradeLevel("4", "4. Sınıf", "İlkokul"),
        GradeLevel("5", "5. Sınıf", "Ortaokul"),
        GradeLevel("6", "6. Sınıf", "Ortaokul"),
        GradeLevel("7", "7. Sınıf", "Ortaokul"),
        GradeLevel("8", "8. Sınıf (LGS)", "Ortaokul"),
        GradeLevel("9", "9. Sınıf", "Lise"),
        GradeLevel("10", "10. Sınıf", "Lise"),
        GradeLevel("11", "11. Sınıf", "Lise"),
        GradeLevel("12", "12. Sınıf", "Lise"),
        GradeLevel("YKS_TYT", "YKS (TYT)", "Sınav Hazırlık"),
        GradeLevel("YKS_AYT", "YKS (AYT)", "Sınav Hazırlık"),
        GradeLevel("KPSS", "KPSS", "Kamu Sınavları")
    )

    val allCourses = listOf(
        // 1. Sınıf
        CourseItem("1_mat", "1. Sınıf Matematik", "1", SubjectBranch.MATHEMATICS, "Rakamlar, temel toplama ve çıkarma", "Tonguç Akademi", listOf("Doğal Sayılar", "Basit Toplama", "Geometrik Şekiller")),
        CourseItem("1_turk", "1. Sınıf Türkçe", "1", SubjectBranch.TURKISH, "Sesler, heceler ve okuma anlama", "Tonguç Akademi", listOf("Harfler & Sesler", "Heceleme", "İlk Okuma Metinleri")),
        CourseItem("1_hayat", "1. Sınıf Hayat Bilgisi", "1", SubjectBranch.SOCIAL, "Okulumuz, ailemiz ve sağlıklı yaşam", "Tonguç Akademi", listOf("Okulda Güvenlik", "Ailemiz", "Kişisel Bakım")),

        // 2. Sınıf
        CourseItem("2_mat", "2. Sınıf Matematik", "2", SubjectBranch.MATHEMATICS, "İki basamaklı sayılar ve çarpma mantığı", "Tonguç Akademi", listOf("Basamak Değeri", "Çarpım Tablosuna Giriş", "Kesirler")),
        CourseItem("2_turk", "2. Sınıf Türkçe", "2", SubjectBranch.TURKISH, "Sözcük bilgisi, eş anlamlılar ve cümle kurma", "Tonguç Akademi", listOf("Eş Anlamlı Kelimeler", "Zıt Anlamlılar", "Noktalama İşaretleri")),
        CourseItem("2_hayat", "2. Sınıf Hayat Bilgisi", "2", SubjectBranch.SOCIAL, "Doğada hayat, ülkemiz ve kurallar", "Tonguç Akademi", listOf("Doğa Olayları", "Bayrağımız", "Trafik Kuralları")),

        // 3. Sınıf
        CourseItem("3_mat", "3. Sınıf Matematik", "3", SubjectBranch.MATHEMATICS, "Üç basamaklı sayılar, bölme ve grafikler", "Tonguç Akademi", listOf("Bölme İşlemi", "Lira ve Kuruş", "Zaman Ölçüleri")),
        CourseItem("3_turk", "3. Sınıf Türkçe", "3", SubjectBranch.TURKISH, "Paragraf anlama ve dil bilgisi", "Tonguç Akademi", listOf("5N1K Metinleri", "Büyük Harflerin Yazımı", "Atasözleri")),
        CourseItem("3_fen", "3. Sınıf Fen Bilimleri", "3", SubjectBranch.SCIENCE, "Maddeyi tanıyalım, kuvvet ve hareket", "Tonguç Akademi", listOf("Gezegenimiz Dünya", "Duyu Organları", "Kuvvet ve Hareket")),

        // 4. Sınıf
        CourseItem("4_mat", "4. Sınıf Matematik", "4", SubjectBranch.MATHEMATICS, "Dört basamaklı sayılar, kesirler ve geometri", "Tonguç Akademi", listOf("Dört İşlem Problemleri", "Kesirlerle İşlemler", "Alan ve Çevre")),
        CourseItem("4_turk", "4. Sınıf Türkçe", "4", SubjectBranch.TURKISH, "Okuma metinleri, ana fikir ve yazım kuralları", "Tonguç Akademi", listOf("Ana Fikir Bulma", "Yazım Kuralları", "Deyimler")),
        CourseItem("4_fen", "4. Sınıf Fen Bilimleri", "4", SubjectBranch.SCIENCE, "Yer kabuğu, besinlerimiz ve aydınlatma", "Tonguç Akademi", listOf("Fosil Oluşumu", "Besin İçerikleri", "Mıknatıs")),
        CourseItem("4_sosyal", "4. Sınıf Sosyal Bilgiler", "4", SubjectBranch.SOCIAL, "Birey ve toplum, kültür ve mirasımız", "Tonguç Akademi", listOf("T.C. Kimlik Kartı", "Milli Kültürümüz", "Yaşadığımız Yer")),

        // 5. Sınıf
        CourseItem("5_mat", "5. Sınıf Matematik", "5", SubjectBranch.MATHEMATICS, "Milyonlar, yüzdeler, ondalık gösterim", "Tonguç Akademi", listOf("Kesirler ve Yüzdeler", "Temel Geometri", "Veri Analizi")),
        CourseItem("5_turk", "5. Sınıf Türkçe", "5", SubjectBranch.TURKISH, "Sözcükte anlam, deyimler ve paragraf", "Tonguç Akademi", listOf("Söz Sanatları", "Paragrafta Yapı", "Metin Türleri")),
        CourseItem("5_fen", "5. Sınıf Fen Bilimleri", "5", SubjectBranch.SCIENCE, "Güneş, Dünya ve Ay; Canlılar dünyası", "Tonguç Akademi", listOf("Ay'ın Evreleri", "Kuvvetin Ölçülmesi", "Maddenin Hâl Değişimi")),
        CourseItem("5_sos", "5. Sınıf Sosyal Bilgiler", "5", SubjectBranch.SOCIAL, "Tarihe yolculuk, doğal afetler ve haklarımız", "Tonguç Akademi", listOf("Anadolu Medeniyetleri", "Çevremizdeki Güzellikler", "Hak ve Sorumluluk")),
        CourseItem("5_yazilim", "5. Sınıf Bilişim & Yazılım", "5", SubjectBranch.SOFTWARE, "Algoritma mantığı ve blok tabanlı kodlama", "Hocalara Geldik", listOf("Algoritma Nedir?", "Scratch ile Kodlama", "Dijital Vatandaşlık")),

        // 6. Sınıf
        CourseItem("6_mat", "6. Sınıf Matematik", "6", SubjectBranch.MATHEMATICS, "Asal sayılar, çarpanlar, tam sayılar ve oran", "Tonguç Akademi", listOf("Çarpanlar ve Katlar", "Kümeler", "Tam Sayılar")),
        CourseItem("6_turk", "6. Sınıf Türkçe", "6", SubjectBranch.TURKISH, "İsimler, sıfatlar, edat ve bağlaçlar", "Tonguç Akademi", listOf("İsim Tamlamaları", "Zamirler", "Paragraf Yorumu")),
        CourseItem("6_fen", "6. Sınıf Fen Bilimleri", "6", SubjectBranch.SCIENCE, "Güneş sistemi, dolaşım ve sindirim sistemleri", "Tonguç Akademi", listOf("Güneş ve Ay Tutulmaları", "Vücudumuzdaki Sistemler", "Yoğunluk")),
        CourseItem("6_sos", "6. Sınıf Sosyal Bilgiler", "6", SubjectBranch.SOCIAL, "İlk Türk devletleri, İpek Yolu ve coğrafya", "Tonguç Akademi", listOf("Orta Asya Türkleri", "İslamiyetin Doğuşu", "Türkiye'nin İklimi")),
        CourseItem("6_yazilim", "6. Sınıf Bilişim & Kodlama", "6", SubjectBranch.SOFTWARE, "Döngüler, değişkenler ve akış şemaları", "Hocalara Geldik", listOf("Akış Şemaları", "Değişkenler", "Siber Güvenlik")),

        // 7. Sınıf
        CourseItem("7_mat", "7. Sınıf Matematik", "7", SubjectBranch.MATHEMATICS, "Tam sayılarla işlemler, rasyonel sayılar, cebir", "Rehber Matematik", listOf("Rasyonel Sayılar", "Cebirsel İfadeler", "Denklemler")),
        CourseItem("7_turk", "7. Sınıf Türkçe", "7", SubjectBranch.TURKISH, "Fiiller, kipler, ek fiil ve zarflar", "Tonguç Akademi", listOf("Haber ve Dilek Kipleri", "Ek Fiil", "Cümlede Anlam")),
        CourseItem("7_fen", "7. Sınıf Fen Bilimleri", "7", SubjectBranch.SCIENCE, "Hücre ve bölünmeler, kuvvet ve enerji, atom", "Tonguç Akademi", listOf("Mitoz ve Mayoz", "Kinetik ve Potansiyel Enerji", "Işığın Kırılması")),
        CourseItem("7_sos", "7. Sınıf Sosyal Bilgiler", "7", SubjectBranch.SOCIAL, "Osmanlı Devleti kuruluşu, fethi ve ıslahatlar", "Tonguç Akademi", listOf("Beylikten Cihan Devletine", "Rönesans ve Reform", "Nüfus ve Göç")),
        CourseItem("7_yazilim", "7. Sınıf Yazılım & Python Giriş", "7", SubjectBranch.SOFTWARE, "Metin tabanlı programlamaya ilk adım", "Hocalara Geldik", listOf("Python Temelleri", "Print ve Input Fonksiyonları", "Koşullu Durumlar (if-else)")),

        // 8. Sınıf (LGS)
        CourseItem("8_mat", "8. Sınıf Matematik (LGS)", "8", SubjectBranch.MATHEMATICS, "Çarpanlar ve katlar, kareköklü ifadeler, olasılık", "Rehber Matematik", listOf("EBOB - EKOK", "Kareköklü Sayılar", "Veri Analizi", "Cebirsel İfadeler ve Özdeşlikler")),
        CourseItem("8_turk", "8. Sınıf Türkçe (LGS)", "8", SubjectBranch.TURKISH, "Fiilimsiler, cümlenin ögeleri, mantık muhakeme", "Tonguç Akademi", listOf("Fiilimsiler", "Cümlenin Ögeleri", "Cümle Türleri", "Sözel Mantık")),
        CourseItem("8_fen", "8. Sınıf Fen Bilimleri (LGS)", "8", SubjectBranch.SCIENCE, "Mevsimler, DNA ve genetik kod, periyodik sistem", "Tonguç Akademi", listOf("Mevsimlerin Oluşumu", "DNA ve Genetik Kod", "Basınç", "Madde ve Endüstri")),
        CourseItem("8_inkilap", "8. Sınıf İnkılap Tarihi (LGS)", "8", SubjectBranch.SOCIAL, "Bir kahraman doğuyor, milli uyanış ve bağımsızlık", "Benim Hocam", listOf("Mustafa Kemal'in Askerlik Hayatı", "Milli Mücadele", "Atatürk İlkeleri")),
        CourseItem("8_yazilim", "8. Sınıf İleri Kodlama & Algoritma", "8", SubjectBranch.SOFTWARE, "Problem çözme algoritmaları ve mini projeler", "Hocalara Geldik", listOf("Diziler & Listeler", "Fonksiyonlar", "Mini Oyun Geliştirme")),

        // 9. Sınıf
        CourseItem("9_mat", "9. Sınıf Matematik", "9", SubjectBranch.MATHEMATICS, "Mantık, kümeler, denklemler ve eşitsizlikler", "Rehber Matematik", listOf("Önermeler ve Mantık", "Kümeler", "Mutlak Değer", "Üçgenler")),
        CourseItem("9_fizik", "9. Sınıf Fizik", "9", SubjectBranch.SCIENCE, "Fizik bilimine giriş, madde ve özellikler, hareket", "Hocalara Geldik", listOf("Özkütle", "Hareket ve Kuvvet", "İş, Güç ve Enerji")),
        CourseItem("9_kimya", "9. Sınıf Kimya", "9", SubjectBranch.SCIENCE, "Kimya bilimi, atom ve periyodik sistem", "Hocalara Geldik", listOf("Kimyasal Türler Arası Etkileşimler", "Maddenin Halleri", "Çevre Kimyası")),
        CourseItem("9_biyo", "9. Sınıf Biyoloji", "9", SubjectBranch.SCIENCE, "Yaşam bilimi biyoloji, hücre ve canlıların sınıflandırılması", "Hocalara Geldik", listOf("Canlıların Ortak Özellikleri", "Hücre Zarı ve Organeller", "Canlılar Alemi")),
        CourseItem("9_edebiyat", "9. Sınıf Türk Dili ve Edebiyatı", "9", SubjectBranch.TURKISH, "Edebiyata giriş, hikaye, şiir ve yazım kuralları", "Hocalara Geldik", listOf("İletişim ve Dil", "Hikaye İnceleme", "Şiir Bilgisi")),
        CourseItem("9_tarih", "9. Sınıf Tarih", "9", SubjectBranch.SOCIAL, "Tarih ve zaman, insanlığın ilk dönemleri", "Benim Hocam", listOf("Tarih Bilimi", "İlk Çağ Uygarlıkları", "İlk Türk Devletleri")),
        CourseItem("9_yazilim", "9. Sınıf Bilgisayar Bilimi & Python", "9", SubjectBranch.SOFTWARE, "Veri yapıları, döngüler ve modüller", "Hocalara Geldik", listOf("Veri Tipleri", "Döngüler", "Modüller ve Kütüphaneler")),

        // 10. Sınıf
        CourseItem("10_mat", "10. Sınıf Matematik", "10", SubjectBranch.MATHEMATICS, "Sayma ve olasılık, fonksiyonlar, polinomlar", "Bıyıklı Matematik", listOf("Permütasyon - Kombinasyon", "Fonksiyon Kavramı", "Polinomlar ve Çarpanlara Ayırma")),
        CourseItem("10_fizik", "10. Sınıf Fizik", "10", SubjectBranch.SCIENCE, "Elektrik ve manyetizma, basınç ve kaldırma kuvveti", "Hocalara Geldik", listOf("Ohm Kanunu", "Manyetizma", "Kaldırma Kuvveti", "Dalgalar")),
        CourseItem("10_kimya", "10. Sınıf Kimya", "10", SubjectBranch.SCIENCE, "Kimyanın temel kanunları, mol kavramı, karışımlar", "Hocalara Geldik", listOf("Kütlenin Korunumu", "Mol Hesaplamaları", "Asitler, Bazlar ve Tuzlar")),
        CourseItem("10_biyo", "10. Sınıf Biyoloji", "10", SubjectBranch.SCIENCE, "Hücre bölünmeleri ve kalıtımın genel ilkeleri", "Hocalara Geldik", listOf("Mitoz ve Eşeysiz Üreme", "Mayoz", "Mendel Genetiği", "Ekoloji")),
        CourseItem("10_edebiyat", "10. Sınıf Türk Dili ve Edebiyatı", "10", SubjectBranch.TURKISH, "Türk edebiyatı dönemleri, destan ve roman", "Hocalara Geldik", listOf("İslamiyet Öncesi Türk Edebiyatı", "Halk Edebiyatı", "Divan Edebiyatı")),
        CourseItem("10_tarih", "10. Sınıf Tarih", "10", SubjectBranch.SOCIAL, "Yerleşme ve devletleşme sürecinde Selçuklu ve Osmanlı", "Benim Hocam", listOf("Anadolu'nun Türkleşmesi", "Beylikten Devlete", "Klasik Dönem Osmanlı")),
        CourseItem("10_yazilim", "10. Sınıf Nesne Yönelimli Programlama", "10", SubjectBranch.SOFTWARE, "Sınıflar, nesneler ve OOP prensipleri", "Hocalara Geldik", listOf("Classes & Objects", "Kalıtım (Inheritance)", "Encapsulation")),

        // 11. Sınıf
        CourseItem("11_mat", "11. Sınıf Matematik", "11", SubjectBranch.MATHEMATICS, "Trigonometri, analitik geometri, fonksiyonlarda uygulamalar", "Rehber Matematik", listOf("Birim Çember & Trigonometrik Fonksiyonlar", "Doğrunun Analitiği", "Parabol ve İkinci Dereceden Eşitsizlikler")),
        CourseItem("11_fizik", "11. Sınıf Fizik", "11", SubjectBranch.SCIENCE, "Kuvvet ve hareket, vektörler, tork ve elektrik alan", "Hocalara Geldik", listOf("İki Boyutta Hareket (Atışlar)", "İtme ve Çizgisel Momentum", "Tork ve Denge", "Elektriksel Kuvvet")),
        CourseItem("11_kimya", "11. Sınıf Kimya", "11", SubjectBranch.SCIENCE, "Modern atom teorisi, gazlar, sıvı çözeltiler ve enerji", "Hocalara Geldik", listOf("Kuantum Sayıları", "İdeal Gaz Yasası", "Tepkimelerde Hız ve Denge")),
        CourseItem("11_biyo", "11. Sınıf Biyoloji", "11", SubjectBranch.SCIENCE, "İnsan fizyolojisi (sinir, endokrin, destek-hareket, dolaşım)", "Hocalara Geldik", listOf("Sinir Sistemi & Duyu Organları", "Hormonlar", "Dolaşım ve Bağışıklık Sistemi")),
        CourseItem("11_edebiyat", "11. Sınıf Türk Dili ve Edebiyatı", "11", SubjectBranch.TURKISH, "Tanzimat, Servet-i Fünun, Milli Edebiyat", "Benim Hocam", listOf("Tanzimat Dönemi Şiir ve Roman", "Milli Edebiyat Akımı", "Cümle Öğeleri")),
        CourseItem("11_tarih", "11. Sınıf Tarih", "11", SubjectBranch.SOCIAL, "Değişen dünya dengeleri karşısında Osmanlı siyaseti", "Benim Hocam", listOf("Westphalia Barışı", "18. Yüzyılda Osmanlı", "Sanayi Devrimi")),
        CourseItem("11_yazilim", "11. Sınıf Web & Veritabanı Geliştirme", "11", SubjectBranch.SOFTWARE, "SQL, REST API ve modern web mimarisi", "Hocalara Geldik", listOf("SQL Sorguları", "RESTful API Mimarisi", "Frontend-Backend Bağlantısı")),

        // 12. Sınıf
        CourseItem("12_mat", "12. Sınıf Matematik", "12", SubjectBranch.MATHEMATICS, "Logaritma, diziler, türev ve integral", "Bıyıklı Matematik", listOf("Üstel Fonksiyon ve Logaritma", "Aritmetik & Geometrik Diziler", "Türev Kuralları ve Uygulamaları", "Belirli & Belirsiz İntegral")),
        CourseItem("12_fizik", "12. Sınıf Fizik", "12", SubjectBranch.SCIENCE, "Çembersel hareket, basit harmonik hareket, dalga mekaniği", "Hocalara Geldik", listOf("Düzgün Çembersel Hareket", "Açısal Momentum", "Basit Harmonik Hareket", "Atom Fiziği")),
        CourseItem("12_kimya", "12. Sınıf Kimya", "12", SubjectBranch.SCIENCE, "Kimya ve elektrik, karbon kimyasına giriş, organik kimya", "Hocalara Geldik", listOf("Elektrokimyasal Piller", "Elektroliz", "Hidrokarbonlar", "Fonksiyonel Gruplar")),
        CourseItem("12_biyo", "12. Sınıf Biyoloji", "12", SubjectBranch.SCIENCE, "Genden proteine, fotosentez, kemosentez, hücresel solunum", "Hocalara Geldik", listOf("Nükleik Asitler ve Protein Sentezi", "Fotosentez ve Kemosentez", "Oksijenli Solunum")),
        CourseItem("12_edebiyat", "12. Sınıf Türk Dili ve Edebiyatı", "12", SubjectBranch.TURKISH, "Cumhuriyet dönemi Türk edebiyatı", "Benim Hocam", listOf("Cumhuriyet Şiiri", "Cumhuriyet Romanı", "Modernist Metinler")),
        CourseItem("12_tarih", "12. Sınıf T.C. İnkılap Tarihi", "12", SubjectBranch.SOCIAL, "20. yüzyıl başlarında Osmanlı, Kurtuluş Savaşı ve Atatürkçülük", "Benim Hocam", listOf("I. Dünya Savaşı", "Kurtuluş Savaşı Muharebeleri", "Atatürk İlkeleri")),
        CourseItem("12_yazilim", "12. Sınıf Mobil & Bulut Teknolojileri", "12", SubjectBranch.SOFTWARE, "Kotlin, Jetpack Compose ve Bulut Sistemleri", "Hocalara Geldik", listOf("Android & Kotlin", "State Management", "Git & Versiyon Kontrolü")),

        // YKS (TYT)
        CourseItem("yks_tyt_mat", "TYT Matematik", "YKS_TYT", SubjectBranch.MATHEMATICS, "Temel kavramlar, problemler, sayılar ve geometri", "Rehber Matematik", listOf("Temel Kavramlar & Basamak", "Rasyonel Sayılar & EBOB-EKOK", "Problemler (Yaş, İşçi, Hız, Kar-Zarar)", "Fonksiyonlar & Kümeler", "Temel Geometri & Üçgenler")),
        CourseItem("yks_tyt_turk", "TYT Türkçe", "YKS_TYT", SubjectBranch.TURKISH, "Sözcükte ve cümlede anlam, paragraf taktikleri ve dil bilgisi", "Benim Hocam", listOf("Sözcükte ve Cümlede Anlam", "Paragrafta Yapı ve Ana Fikir", "Yazım Kuralları ve Noktalama", "Ses Bilgisi ve Sözcük Türleri")),
        CourseItem("yks_tyt_fen", "TYT Fen Bilimleri", "YKS_TYT", SubjectBranch.SCIENCE, "Temel Fizik, Kimya ve Biyoloji", "Hocalara Geldik", listOf("Fizik: Madde, Hareket, Isı-Sıcaklık, Optik", "Kimya: Atom, Periyodik Tablo, Karışımlar", "Biyoloji: Canlıların Ortak Özellikleri, Hücre, Kalıtım")),
        CourseItem("yks_tyt_sosyal", "TYT Sosyal Bilimler", "YKS_TYT", SubjectBranch.SOCIAL, "Tarih, Coğrafya, Felsefe ve Din Kültürü", "Benim Hocam", listOf("Tarih: İlk Çağ, İslamiyet, Osmanlı, Kurtuluş Savaşı", "Coğrafya: Harita Bilgisi, İklim, Nüfus", "Felsefe ve Din Kültürü Temel Kavramları")),
        CourseItem("yks_tyt_yazilim", "Algoritmik Düşünme & Mantık", "YKS_TYT", SubjectBranch.SOFTWARE, "Sayısal mantık ve algoritma soruları", "Hocalara Geldik", listOf("Grafikler ve Tablo Yorumlama", "Akış Şemaları", "Sayısal Mantık Soruları")),

        // YKS (AYT)
        CourseItem("yks_ayt_mat", "AYT Matematik", "YKS_AYT", SubjectBranch.MATHEMATICS, "Polinomlar, trigonometri, logaritma, limit, türev, integral", "Bıyıklı Matematik", listOf("Polinomlar ve II. Dereceden Denklemler", "İleri Trigonometri", "Logaritma ve Diziler", "Limit ve Süreklilik", "Türev ve Uygulamaları", "İntegral ve Alan")),
        CourseItem("yks_ayt_geo", "AYT Geometri", "YKS_AYT", SubjectBranch.MATHEMATICS, "Üçgenler, çokgenler, çember, analitik ve katı cisimler", "Rehber Matematik", listOf("Özel Üçgenler ve Alan", "Dörtgenler ve Çember", "Analitik Geometri", "Katı Cisimler")),
        CourseItem("yks_ayt_fizik", "AYT Fizik", "YKS_AYT", SubjectBranch.SCIENCE, "Vektörler, mekanik, elektrik, manyetizma ve modern fizik", "Hocalara Geldik", listOf("Vektörler & Atışlar", "Tork & Denge", "Elektriksel Kuvvet ve Potansiyel", "Manyetizma ve Alternatif Akım", "Fotoelektrik ve Atom Modelleri")),
        CourseItem("yks_ayt_kimya", "AYT Kimya", "YKS_AYT", SubjectBranch.SCIENCE, "Modern atom teorisi, gazlar, termodinamik, denge, organik kimya", "Hocalara Geldik", listOf("Gaz Yasaları", "Kimyasal Denge & Çözünürlük", "Elektrokimya", "Organik Bileşikler")),
        CourseItem("yks_ayt_biyo", "AYT Biyoloji", "YKS_AYT", SubjectBranch.SCIENCE, "Sistemler, genetik, fotosentez ve solunum", "Hocalara Geldik", listOf("İnsan Fizyolojisi", "Nükleik Asitler & Protein Sentezi", "Fotosentez & Hücresel Solunum", "Bitki Biyolojisi")),
        CourseItem("yks_ayt_edebiyat", "AYT Edebiyat", "YKS_AYT", SubjectBranch.TURKISH, "Şiir bilgisi, edebi sanatlar, dönemler ve yazarlar", "Benim Hocam", listOf("Şiir Bilgisi & Edebi Sanatlar", "Halk & Divan Edebiyatı", "Tanzimat'tan Cumhuriyete Akımlar", "Eser-Yazar Eşleştirmeleri")),
        CourseItem("yks_ayt_tarih", "AYT Tarih", "YKS_AYT", SubjectBranch.SOCIAL, "Tarih-1 ve Tarih-2 detaylı konu anlatımı", "Benim Hocam", listOf("İlk Türk İslam Devletleri", "Osmanlı Siyasi Tarihi", "Atatürk Dönemi Dış Politika")),
        CourseItem("yks_ayt_cografya", "AYT Coğrafya", "YKS_AYT", SubjectBranch.SOCIAL, "Ekosistemler, Türkiye ekonomisi ve küresel bölgeler", "Benim Hocam", listOf("Biyoçeşitlilik", "Türkiye'de Tarım ve Hayvancılık", "Küresel Ticaret ve Örgütler")),

        // KPSS
        CourseItem("kpss_genel_yetenek_turk", "KPSS Türkçe (Genel Yetenek)", "KPSS", SubjectBranch.TURKISH, "Sözcükte anlam, paragraf yorumlama, sözel mantık", "Benim Hocam", listOf("Sözcük ve Cümle Anlamı", "Paragraf Yorumlama", "Anlatım Bozuklukları", "Sözel Mantık")),
        CourseItem("kpss_genel_yetenek_mat", "KPSS Matematik & Mantık", "KPSS", SubjectBranch.MATHEMATICS, "Temel matematik, problemler ve sayısal mantık", "Bıyıklı Matematik", listOf("Sayılar ve Bölünebilme", "Oran-Orantı ve Problemler", "Kümeler ve Olasılık", "Sayısal Mantık")),
        CourseItem("kpss_tarih", "KPSS Tarih (Genel Kültür)", "KPSS", SubjectBranch.SOCIAL, "İslamiyet öncesi, Selçuklu, Osmanlı ve İnkılap tarihi", "Benim Hocam", listOf("İslamiyet Öncesi Türk Tarihi", "Osmanlı Kültür ve Medeniyeti", "Milli Mücadele Dönemi", "Çağdaş Türk ve Dünya Tarihi")),
        CourseItem("kpss_cografya", "KPSS Türkiye Coğrafyası", "KPSS", SubjectBranch.SOCIAL, "Türkiye'nin fiziki, beşeri ve ekonomik özellikleri", "Benim Hocam", listOf("Türkiye'nin Yer Şekilleri ve İklimi", "Nüfus ve Yerleşme", "Tarım, Sanayi ve Madenler")),
        CourseItem("kpss_vatandaslik", "KPSS Vatandaşlık & Anayasa", "KPSS", SubjectBranch.SOCIAL, "Temel hukuk, 1982 Anayasası, idare hukuku ve güncel olaylar", "Benim Hocam", listOf("Temel Hukuk Kavramları", "Anayasa Tarihi ve Yasama-Yürütme-Yargı", "İdare Hukuku", "Güncel Bilgiler")),
        CourseItem("kpss_egitim", "KPSS Eğitim Bilimleri", "KPSS", SubjectBranch.SOCIAL, "Gelişim psikolojisi, öğrenme psikolojisi ve ÖYT", "Benim Hocam", listOf("Gelişim Psikolojisi", "Öğrenme Psikolojisi", "Öğretim Yöntem ve Teknikleri (ÖYT)", "Ölçme ve Değerlendirme")),
        CourseItem("kpss_yazilim", "KPSS Bilişim & Teknoloji", "KPSS", SubjectBranch.SOFTWARE, "Kamuda dijital dönüşüm, veri güvenliği ve e-Devlet", "Hocalara Geldik", listOf("Bilişim Kavramları", "Bilgi Güvenliği", "Veri Tabanları & e-Devlet"))
    )

    fun getCourses(gradeId: String, branch: SubjectBranch): List<CourseItem> {
        return allCourses.filter { course ->
            (gradeId.isEmpty() || course.gradeId == gradeId) &&
            (branch == SubjectBranch.ALL || course.branch == branch)
        }
    }
}
