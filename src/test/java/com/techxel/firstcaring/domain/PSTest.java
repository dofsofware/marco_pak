package com.techxel.firstcaring.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.techxel.firstcaring.web.rest.TestUtil;

public class PSTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PS.class);
        PS pS1 = new PS();
        pS1.setId(1L);
        PS pS2 = new PS();
        pS2.setId(pS1.getId());
        assertThat(pS1).isEqualTo(pS2);
        pS2.setId(2L);
        assertThat(pS1).isNotEqualTo(pS2);
        pS1.setId(null);
        assertThat(pS1).isNotEqualTo(pS2);
    }
}
