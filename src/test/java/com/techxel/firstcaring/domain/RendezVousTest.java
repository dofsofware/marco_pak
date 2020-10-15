package com.techxel.firstcaring.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.techxel.firstcaring.web.rest.TestUtil;

public class RendezVousTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RendezVous.class);
        RendezVous rendezVous1 = new RendezVous();
        rendezVous1.setId(1L);
        RendezVous rendezVous2 = new RendezVous();
        rendezVous2.setId(rendezVous1.getId());
        assertThat(rendezVous1).isEqualTo(rendezVous2);
        rendezVous2.setId(2L);
        assertThat(rendezVous1).isNotEqualTo(rendezVous2);
        rendezVous1.setId(null);
        assertThat(rendezVous1).isNotEqualTo(rendezVous2);
    }
}
