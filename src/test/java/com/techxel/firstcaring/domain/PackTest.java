package com.techxel.firstcaring.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.techxel.firstcaring.web.rest.TestUtil;

public class PackTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pack.class);
        Pack pack1 = new Pack();
        pack1.setId(1L);
        Pack pack2 = new Pack();
        pack2.setId(pack1.getId());
        assertThat(pack1).isEqualTo(pack2);
        pack2.setId(2L);
        assertThat(pack1).isNotEqualTo(pack2);
        pack1.setId(null);
        assertThat(pack1).isNotEqualTo(pack2);
    }
}
