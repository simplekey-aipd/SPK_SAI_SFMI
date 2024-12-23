package com.simplekey.spk_sai_sfmi.api.sfmi.mpc.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Twb_bas03Id implements Serializable {
    private String groupCd;
    private String cd;
    private String cdType;
    private String cenTypeCd;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Twb_bas03Id that = (Twb_bas03Id) o;

        if (!groupCd.equals(that.groupCd)) return false;
        if (!cd.equals(that.cd)) return false;
        if (!cdType.equals(that.cdType)) return false;
        return cenTypeCd.equals(that.cenTypeCd);
    }

    @Override
    public int hashCode() {
        int result = groupCd.hashCode();
        result = 31 * result + cd.hashCode();
        result = 31 * result + cdType.hashCode();
        result = 31 * result + cenTypeCd.hashCode();
        return result;
    }
}
