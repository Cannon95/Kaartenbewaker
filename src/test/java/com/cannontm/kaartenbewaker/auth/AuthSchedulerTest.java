package com.cannontm.kaartenbewaker.auth;

import com.cannontm.kaartenbewaker.auth.AuthScheduler;
import com.cannontm.kaartenbewaker.auth.AuthToken;
import com.cannontm.kaartenbewaker.auth.AuthTokenService;
import com.cannontm.kaartenbewaker.dto.JobMessage;
import com.cannontm.kaartenbewaker.dto.JobType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthSchedulerTest {

  @Mock
  private AuthTokenService authTokenService;

  @InjectMocks
  private AuthScheduler authScheduler;

  @Test
  void debugTest() {
    System.out.println("test running");
  }

  @ParameterizedTest(name = "Expired token scenario {index}")
  @MethodSource("expiredTokenProvider")
  void shouldLoginWhenAnyTokenExpired(AuthToken token) {

    when(authTokenService.getToken()).thenReturn(token);

    StepVerifier.withVirtualTime(() -> authScheduler.produceJobs().take(1))
        .thenAwait(Duration.ofSeconds(1))
        .expectNextMatches(jobMessage -> jobMessage.getType() == JobType.LOGIN)
        .thenCancel()
        .verify();
  }

  static Stream<TokenTestCase> expiredTokenProvider() {
    long now = Instant.now().getEpochSecond();

    return Stream.of(
        new TokenTestCase("both_expired",
            AuthToken.builder()
                .expiresAtLive(now - 1000)
                .expiresAtCore(now - 1000)
                .refreshableAtCore(now + 1000)
                .refreshableAtLive(now + 1000)
                .build(),
            JobType.LOGIN),
        new TokenTestCase("live_expired",
            AuthToken.builder()
                .expiresAtLive(now - 1000)
                .expiresAtCore(now + 1000)
                .refreshableAtCore(now + 1000)
                .refreshableAtLive(now + 1000)
                .build(),
            JobType.LOGIN),
        new TokenTestCase("core_expired",
            AuthToken.builder()
                .expiresAtLive(now + 1000)
                .expiresAtCore(now - 1000)
                .refreshableAtCore(now + 1000)
                .refreshableAtLive(now + 1000)
                .build(),
            JobType.LOGIN));

  }
}
