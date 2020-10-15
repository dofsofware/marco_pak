package com.techxel.firstcaring.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.techxel.firstcaring.web.rest.TestUtil;

public class RapportPharmacienTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RapportPharmacien.class);
        RapportPharmacien rapportPharmacien1 = new RapportPharmacien();
        rapportPharmacien1.setId(1L);
        RapportPharmacien rapportPharmacien2 = new RapportPharmacien();
        rapportPharmacien2.setId(rapportPharmacien1.getId());
        assertThat(rapportPharmacien1).isEqualTo(rapportPharmacien2);
        rapportPharmacien2.setId(2L);
        assertThat(rapportPharmacien1).isNotEqualTo(rapportPharmacien2);
        rapportPharmacien1.setId(null);
        assertThat(rapportPharmacien1).isNotEqualTo(rapportPharmacien2);
    }
}
