# DHX Courier delivery services
Proof of concept - simple courier delivery services database management using Cassandra and InfluxDB
# 🎥 Short video of presentation:
https://www.youtube.com/watch?v=SDstrfwLkv4&feature=youtu.be

```
CREATE TABLE IF NOT EXISTS klienci (
	klient_id timeuuid,
  	imie text,
  	nazwisko text,
  	miasto text,
  	ulica text,
  	numer_domu int,
  	telefon int,
  	PRIMARY KEY ((telefon),klient_id)
) WITH CLUSTERING ORDER BY (klient_id desc);

CREATE TABLE IF NOT EXISTS kierowcy (
	kierowcy_id timeuuid,
  	imie text,
  	nazwisko text,
  	telefon int,
  	PRIMARY KEY ((telefon),kierowcy_id)
) WITH CLUSTERING ORDER BY (kierowcy_id desc);

CREATE TABLE IF NOT EXISTS paczki (
	paczka_id timeuuid,
  	klient_id timeuuid,
  	kierowcy_id timeuuid,
    miasto text,
  	ulica text,
  	numer_domu int,
  	ilosc_paczek int,
	pobranie float,
	identyfikator_rejonu_paczki int,
  	PRIMARY KEY ((identyfikator_rejonu_paczki),paczka_id)
) WITH CLUSTERING ORDER BY (paczka_id desc);
```
