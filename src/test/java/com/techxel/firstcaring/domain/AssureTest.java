package com.techxel.firstcaring.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.techxel.firstcaring.web.rest.TestUtil;

public class AssureTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assure.class);
        Assure assure1 = new Assure();
        assure1.setId(1L);
        Assure assure2 = new Assure();
        assure2.setId(assure1.getId());
        assertThat(assure1).isEqualTo(assure2);
        assure2.setId(2L);
        assertThat(assure1).isNotEqualTo(assure2);
        assure1.setId(null);
        assertThat(assure1).isNotEqualTo(assure2);
    }
}
