/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2024 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.cactoos.func;

import java.io.EOFException;
import java.io.IOException;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.llorllale.cactoos.matchers.Assertion;

/**
 * Test case for {@link CheckedBiFunc}.
 *
 * @since 0.32
 * @checkstyle JavadocMethodCheck (500 lines)
 */
public final class CheckedBiFuncTest {

    @Test(expected = IllegalStateException.class)
    public void runtimeExceptionIsNotWrapped() throws Exception {
        new CheckedBiFunc<>(
            (first, second) -> {
                throw new IllegalStateException("runtime1");
            },
            IOException::new
        ).apply(true, true);
    }

    @Test(expected = IOException.class)
    public void checkedExceptionIsWrapped() throws Exception {
        new CheckedBiFunc<>(
            (first, second) -> {
                throw new EOFException("runtime2");
            },
            IOException::new
        ).apply(true, true);
    }

    @Test
    public void extraWrappingIgnored() {
        try {
            new CheckedBiFunc<>(
                (first, second) -> {
                    throw new IOException("runtime3");
                },
                IOException::new
            ).apply(true, true);
        } catch (final IOException exp) {
            new Assertion<>(
                "Extra wrapping of IOException has been added",
                exp.getCause(),
                new IsNull<>()
            ).affirm();
        }
    }

    @Test
    public void noExceptionThrown() throws Exception {
        new Assertion<>(
            "Must not throw an exception",
            new CheckedBiFunc<>(
                (first, second) -> true,
                exp -> exp
            ).apply(false, false),
            new IsEqual<>(true)
        ).affirm();
    }

}
