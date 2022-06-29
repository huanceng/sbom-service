package org.openeuler.sbom.manager.model.spdx;

public enum Algorithm {
    MD2(32),
    MD4(32),
    MD5(32),
    MD6(-1),
    SHA1(40),
    SHA224(56),
    SHA256(64),
    SHA384(96),
    SHA512(128);

    private final Integer checksumHexDigits;

    Algorithm(Integer checksumHexDigits) {
        this.checksumHexDigits = checksumHexDigits;
    }

    public Integer getChecksumHexDigits() {
        return checksumHexDigits;
    }
}
