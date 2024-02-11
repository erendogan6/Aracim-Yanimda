# Aracım Yanımda - Araç Kiralama Uygulaması
<img src="https://i.hizliresim.com/i2pqliw.png" alt0="Logo" width="170" height="170">

## Proje Hakkında
**"Aracım Yanımda"**, kullanıcıların Android cihazları üzerinden kolaylıkla erişebildikleri ve mevcut konumlarına yakın kiralık araçları bulmalarını, rezervasyon yapmalarını ve mevcut rezervasyonlarını yönetmelerini sağlayan, Java ile geliştirilmiş kapsamlı bir araç kiralama uygulamasıdır. Uygulama, araç kiralama sürecini kullanıcı için mümkün olan en sorunsuz ve etkili hale getirmek için tasarlanmıştır.
Kullanıcıların, geniş bir araç yelpazesinden seçim yapabilmeleri ve istedikleri zaman diliminde araç rezervasyonu gerçekleştirebilmeleri için tasarlanan bu uygulama, harita tabanlı bir arayüz sunarak, araçların mevcut konumlarına göre kolayca bulunmasını sağlar.

<img src="https://github.com/erendogan6/Aracim-Yanimda/blob/main/AracimYanimda.gif" alt0="Logo" width="300" height="640">

## Azure Entegrasyonu
Uygulamanın altyapısı, Microsoft Azure üzerinde barındırılmaktadır. Azure API Management, uygulama ve kullanıcıları arasında güvenli ve etkili bir iletişim köprüsü kurarken, Azure SQL Database (MySQL), uygulamanın veri depolama ihtiyaçlarını karşılar. Bu entegrasyon, uygulamanın yüksek performanslı, güvenilir ve ölçeklenebilir olmasını sağlar. Ayrıca, Azure'un sağladığı güvenlik ve yönetim özellikleri, kullanıcı verilerinin korunmasında ve uygulamanın sorunsuz çalışmasında hayati rol oynar.

## Web Uygulaması Hakkında
"Aracım Yanımda" mobil uygulaması, aynı zamanda yöneticilere ve araç kiralama firmalarının sahiplerine yönelik bir web platformu ile tam entegre çalışmaktadır. Bu entegrasyon sayesinde, yöneticiler araç ekleme, çıkarma ve rezervasyon yönetimi gibi işlemleri web üzerinden gerçekleştirebilirken, kullanıcılar da mobil uygulama üzerinden bu değişiklikleri gerçek zamanlı olarak görebilir ve buna göre rezervasyon yapabilirler. Bu bütünleşik sistem, araç kiralama sürecini hem yönetici hem de kullanıcı için daha verimli ve erişilebilir hale getirir. Web uygulamasına buradan ulaşabilirsiniz: [Aracım Yanımda Web Uygulaması](https://github.com/Kkyyasin/AracYanimda) 


## Özellikler
- **Konum Bazlı Arama**: Kullanıcıların bulundukları konuma yakın kiralık araçları harita üzerinde görüntülemelerini sağlar.
- **Esnek Rezervasyon**: Kullanıcılar, istedikleri tarih ve saat aralıkları için kolayca araç rezervasyonu yapabilirler.
- **Rezervasyon Yönetimi**: Mevcut rezervasyonlar kolaylıkla görüntülenebilir, düzenlenebilir ve iptal edilebilir.
- **Kullanıcı Dostu Arayüz**: Kolay ve anlaşılır bir kullanıcı arayüzü ile hızlı bir şekilde istediğiniz işlemleri gerçekleştirebilirsiniz.
- **Rezervasyon Geçmişi**: Kullanıcılar, geçmiş rezervasyonlarını görüntüleyebilir ve gerektiğinde detaylarına erişebilirler.
- **Anında Rezervasyon Onayı**: Rezervasyon yaptıktan sonra anında onay alabilir ve planlarınızı güvenle yapabilirsiniz.
- **Rezervasyon Bitirme**: Kiralama sürecinin sonunda, uygulama üzerinden kolayca rezervasyon bitirme işlemleri yapılabilmektedir.
- **Kart Bilgilerinin Kaydedilmesi**: Kullanıcılar, kredi kartı bilgilerini güvenli bir şekilde uygulamada kaydedebilir ve rezervasyon ödemelerini istedikleri kayıtlı kart ile gerçekleştirebilir.
- **Ehliyet Bilgisi Kaydı**: Kullanıcılar, uygulamaya ehliyet bilgilerini kaydedebilir, böylece kiralama işlemleri sırasında bu bilgilerin tekrar girilmesine gerek kalmaz.
- **API KEY Gizlenmemiştir**

## Teknolojiler
- Java
- Retrofit
- RxJava
- Azure API Management
- Azure SQL Database (MySQL)
- Google Maps
- Singleton Design Pattern
- Android Jetpack (Navigation, LiveData, ViewModel)
- RecyclerView
- GSON
- UI/UX Design Principles
- Fragment Management
- View Binding
- Composite Disposable

## Test Edilen Sürümler
- Android 9.0
- Android 11.0
- Android 13.0
- Android 14.0

## Kurulum

Projeyi yerel olarak çalıştırmak için aşağıdaki adımları takip edin:

```bash
git clone https://github.com/erendogan6/Aracim-Yanimda.git
cd <projeadi>
# Android Studio'nun en son sürümünü kullanarak projeyi açın ve gerekli bağımlılıkları yükleyin.
# API KEY Gizlenmemiştir. API Key'i değiştirerek kullanabilirsiniz.
# Uygulamayı bir Android cihazda veya emülatörde çalıştırabilirsiniz.
```
Uygulamayı doğrudan Android cihazınızda çalıştırabilmek için "Aracim_Yanimda.apk" dosyasını indirip uygulamayı cihazınıza kurabilirsiniz.

## Katkıda Bulunma ##

Projeye katkıda bulunmak isteyenler için katkı kuralları ve adımları CONTRIBUTING.md dosyasında açıklanmıştır.

##  Lisans ## 
Bu proje MIT Lisansı altında lisanslanmıştır.
