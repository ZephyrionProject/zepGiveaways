<div align="center">
  <h1>🎁 zepGiveaways</h1>
  <p><strong>Gelişmiş, Lagsız ve Modern Kelime Tabanlı Çekiliş Eklentisi [1.21]</strong></p>
</div>

---

Sunucunuzdaki klasik ve sıkıcı çekiliş eklentilerinden sıkıldınız mı? Sohbeti kirletmeyen, TPS düşürmeyen ve direkt elinizdeki eşyayı anında oyunculara çekilişle dağıtabilmenizi sağlayan **zepGiveaways** ile tanışın! Geliştiriciliğini **Redted**'in üstlendiği bu eklenti, modern Paper 1.21 sunucuları için sıfırdan, yüksek performansla kodlanmıştır.

## 🚀 Öne Çıkan Özellikler

- **Eldeki Eşyayı Anında Ödül Yapın**: Oyuncularınıza özel bir kılıç veya 64x Elmas mı dağıtmak istiyorsunuz? Eşyayı elinize alın ve komutu girin. Eklenti, elinizdeki eşyanın tüm büyülerini, isimlerini ve lore özelliklerini kopyalayarak çekiliş ödülü olarak belirler.
- **Sessiz & Spamsız Katılım Sistemi**: Oyuncular çekilişe katılmak için chat alanına sizin belirlediğiniz kelimeyi yazar. Sohbet tertemiz kalır; "Katıldınız!" mesajlarıyla spam oluşmaz.
- **Sıfır Lag! (Asenkron Mimari)**: Katılım işlemleri tamamen Asenkron (AsyncChatEvent) olarak yönetilir. Eşzamanlı 1000 katılımcı bile TPS düşüşü yaratmaz.
- **Hata Korumalı Teslimat**: Kazananın envanteri doluysa eşyayı asla yere düşürmez. Kazanan oyuncuya ve çekilişi başlatan yetkiliye anında özel bildirim gider.
- **Animasyonlu Title & Geri Sayım**: Süre boyunca ekranın ortasında sabit bir başlık kalır, son 10 saniyede geri sayım ses efektleriyle başlar.
- **Kyori MiniMessage Desteği**: Modern renk geçişleri, RGB ve Hex kodlarını `%100` destekler. (Legacy `§` kodları sistem tarafından otomatik düzeltilir.)

## 💻 Komutlar ve Yetkiler

| Komut | Açıklama | Yetki |
|---|---|---|
| `/cekilis baslat <kelime> <süre>` | Çekilişi başlatır. (Örn: `1m`, `30s`, `1h`) | `cekilis.admin` |
| `/cekilis bitir` | Devam eden çekilişi anında sonlandırır. | `cekilis.admin` |
| `/cekilis reload` | `config.yml` ve `lang.yml` dosyalarını yeniler. | `cekilis.admin` |

## ⚙️ Gereksinimler
- **Java Sürümü**: Java 21+
- **Sunucu Yazılımı**: Paper 1.21 ve üzeri

## 📥 Kurulum
1. Eklentinin derlenmiş `.jar` dosyasını sunucunuzun `plugins/` klasörüne kopyalayın.
2. Sunucunuzu başlatın veya yeniden başlatın.
3. `plugins/zepGiveaways/` dizininde oluşan `lang.yml` ve `config.yml` dosyalarından mesaj ve ayarlarınızı dilediğiniz gibi düzenleyin.

## 🛠️ Nasıl Derlenir? (Geliştiriciler İçin)
Eklenti Maven kullanılarak derlenebilir. Proje dizininde komut satırını açarak aşağıdaki komutu girmeniz yeterlidir:
```bash
mvn clean package
```
Çıktı olarak `target/` klasörü içerisinde `zepGiveaways-1.0.0.jar` dosyası oluşacaktır.

---
*Geliştirici: Redted | Lisans: MIT / Açık Kaynak*
