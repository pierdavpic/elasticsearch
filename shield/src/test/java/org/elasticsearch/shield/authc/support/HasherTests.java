/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.shield.authc.support;

import org.elasticsearch.test.ElasticsearchTestCase;
import org.junit.Test;

import static org.hamcrest.Matchers.sameInstance;

/**
 *
 */
public class HasherTests extends ElasticsearchTestCase {

    @Test
    public void testBcryptFamily_SelfGenerated() throws Exception {
        testHasherSelfGenerated(Hasher.BCRYPT);
        testHasherSelfGenerated(Hasher.BCRYPT4);
        testHasherSelfGenerated(Hasher.BCRYPT5);
        testHasherSelfGenerated(Hasher.BCRYPT6);
        testHasherSelfGenerated(Hasher.BCRYPT7);
        testHasherSelfGenerated(Hasher.BCRYPT8);
        testHasherSelfGenerated(Hasher.BCRYPT9);
    }

    @Test
    public void testMd5_SelfGenerated() throws Exception {
        testHasherSelfGenerated(Hasher.MD5);
    }

    @Test
    public void testSha1_SelfGenerated() throws Exception {
        testHasherSelfGenerated(Hasher.SHA1);
    }

    @Test
    public void testSSHA256_SelfGenerated() throws Exception {
        testHasherSelfGenerated(Hasher.SSHA256);
    }

    @Test
    public void testNoop_SelfGenerated() throws Exception {
        testHasherSelfGenerated(Hasher.NOOP);
    }

    @Test
    public void testResolve() throws Exception {
        assertThat(Hasher.resolve("bcrypt"), sameInstance(Hasher.BCRYPT));
        assertThat(Hasher.resolve("bcrypt4"), sameInstance(Hasher.BCRYPT4));
        assertThat(Hasher.resolve("bcrypt5"), sameInstance(Hasher.BCRYPT5));
        assertThat(Hasher.resolve("bcrypt6"), sameInstance(Hasher.BCRYPT6));
        assertThat(Hasher.resolve("bcrypt7"), sameInstance(Hasher.BCRYPT7));
        assertThat(Hasher.resolve("bcrypt8"), sameInstance(Hasher.BCRYPT8));
        assertThat(Hasher.resolve("bcrypt9"), sameInstance(Hasher.BCRYPT9));
        assertThat(Hasher.resolve("sha1"), sameInstance(Hasher.SHA1));
        assertThat(Hasher.resolve("md5"), sameInstance(Hasher.MD5));
        assertThat(Hasher.resolve("ssha256"), sameInstance(Hasher.SSHA256));
        assertThat(Hasher.resolve("noop"), sameInstance(Hasher.NOOP));
        assertThat(Hasher.resolve("clear_text"), sameInstance(Hasher.NOOP));
        try {
            Hasher.resolve("unknown_hasher");
            fail("expected a shield setting error when trying to resolve an unknown hasher");
        } catch (IllegalArgumentException e) {
            // expected
        }
        Hasher hasher = randomFrom(Hasher.values());
        assertThat(Hasher.resolve("unknown_hasher", hasher), sameInstance(hasher));
    }

    private static void testHasherSelfGenerated(Hasher hasher) throws Exception {
        SecuredString passwd = SecuredStringTests.build(randomAsciiOfLength(10));
        char[] hash = hasher.hash(passwd);
        assertTrue(hasher.verify(passwd, hash));
    }
}
