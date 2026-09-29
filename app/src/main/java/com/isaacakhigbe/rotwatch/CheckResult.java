package com.isaacakhigbe.rotwatch;

public sealed interface CheckResult permits Response, Failure {
}

record Response(int statusCode) implements CheckResult {
}

record Failure(String reason) implements CheckResult {
}
