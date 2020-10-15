package com.techxel.firstcaring.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.techxel.firstcaring.web.rest.TestUtil;

public class AssureurTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assureur.class);
        Assureur assureur1 = new Assureur();
        assureur1.setId(1L);
        Assureur assureur2 = new Assureur();
        assureur2.setId(assureur1.getId());
        assertThat(assureur1).isEqualTo(assureur2);
        assureur2.setId(2L);
        assertThat(assureur1).isNotEqualTo(assureur2);
        assureur1.setId(null);
        assertThat(assureur1).isNotEqualTo(assureur2);
    }
}
