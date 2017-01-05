package com.advertiser.domain.enumeration;

import lombok.Getter;

/**
 * Created by ws on 1/5/17.
 */
@Getter
public enum CampaignBusiness {
    rolnictwo_leśnictwo_łowiectwo_i_rybactwo("rolnictwo, leśnictwo, łowiectwo i rybactwo"),
    górnictwo_i_wydobywanie("górnictwo i wydobywanie"),
    przetwórstwo_przemysłowe("przetwórstwo przemysłowe"),
    energia_elektryczna_gaz_para_wodna_gorąca_woda("energia elektryczna, gaz, para wodna, gorąca woda"),
    wody_ścieki_odpady_oraz_rekultywacja("wody, ścieki, odpady oraz rekultywacja"),
    budownictwo("budownictwo"),
    handel_hurtowy_i_detaliczny_naprawa_pojazdów_i_motocykli("handel hurtowy i detaliczny; naprawa pojazdów i motocykli"),
    transport_i_gospodarka_magazynowa("transport i gospodarka magazynowa"),
    zakwaterowanie_i_usługi_gastronomiczne("zakwaterowanie i usługi gastronomiczne"),
    informacja_i_komunikacja("informacja i komunikacja"),
    finanse_i_ubezpieczenia("finanse i ubezpieczenia"),
    obsługa_rynku_nieruchomości("obsługa rynku nieruchomości"),
    działalność_profesjonalna_naukowa_i_techniczna("działalność profesjonalna, naukowa i techniczna"),
    usługi_administrowania_i_działalność_wspierająca("usługi administrowania i działalność wspierająca"),
    administracja_publiczna_i_obrona_narodowa("administracja publiczna i obrona narodowa"),
    edukacja("edukacja"),
    opieka_zdrowotna_i_pomoc_społeczna("opieka zdrowotna i pomoc społeczna"),
    kultura_rozrywka_i_rekreacja("kultura, rozrywka i rekreacja"),
    inna_działalność_usługowa("inna działalność usługowa"),
    gospodarstwa_domowe("gospodarstwa domowe"),
    organizacje_i_zespoły_eksterytorialne("organizacje i zespoły eksterytorialne");

    String name;
    CampaignBusiness(String name){
        this.name = name;
    }
}
