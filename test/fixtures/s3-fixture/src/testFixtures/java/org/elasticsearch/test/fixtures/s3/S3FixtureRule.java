/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.test.fixtures.s3;

import org.jetbrains.annotations.NotNull;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;

public class S3FixtureRule implements S3Fixture {

    private boolean useFixture;
    private S3Fixture delegate;
    private final List<String> services = new ArrayList<>();

    public S3FixtureRule(boolean useFixture) {
        this.useFixture = useFixture;
    }

    @NotNull
    @Override
    public Statement apply(@NotNull Statement base, @NotNull Description description) {
        delegate = (useFixture && services.isEmpty() == false)  ? new S3FixtureTestContainerRule(services) : new S3ExternalRule();
        delegate.apply(base, description);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } finally {

                }
            }
        };
    }

    @Override
    public S3Fixture withExposedService(String serviceName) {
        if (delegate == null) {
            this.services.add(serviceName);
        } else {
            delegate.withExposedService(serviceName);
        }
        return this;
    }

    @Override
    public String getServiceUrl(String serviceName) {
        return delegate.getServiceUrl(serviceName);
    }
}
