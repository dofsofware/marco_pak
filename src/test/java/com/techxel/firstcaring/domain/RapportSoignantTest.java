package com.techxel.firstcaring.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.techxel.firstcaring.web.rest.TestUtil;

public class RapportSoignantTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RapportSoignant.class);
        RapportSoignant rapportSoignant1 = new RapportSoignant();
        rapportSoignant1.setId(1L);
        RapportSoignant rapportSoignant2 = new RapportSoignant();
        rapportSoignant2.setId(rapportSoignant1.getId());
        assertThat(rapportSoignant1).isEqualTo(rapportSoignant2);
        rapportSoignant2.setId(2L);
        assertThat(rapportSoignant1).isNotEqualTo(rapportSoignant2);
        rapportSoignant1.setId(null);
        assertThat(rapportSoignant1).isNotEqualTo(rapportSoignant2);
    }
}
