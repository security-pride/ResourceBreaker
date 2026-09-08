================================================================================
C2 Boot-Time Query Analysis
================================================================================
T0 (first query): 04-15 20:27:10.041
First safeMode=true snapshot: +797ms
First safeMode=true query: +701ms
Total queries: 412

## Summary Statistics (Table c2-stats)

| Metric | Value |
|--------|-------|
| Total queryIntent*Internal invocations | 412 |
| Before safeMode activation (30): | |
|   Self-protected (MSO = true) | 18 |
|   **Unprotected (MSO = false)** | **12** |
| After safeMode activation (382): | |
|   Protected (MSO = true) | 382 |
| Unprotected queries with GET_META_DATA | 6 |
| Distinct calling services (unprotected) | 7 |
| Race window | 797 ms |

## C2 Violations (Table c2-violations)

| # | Δt (ms) | Caller | Intent | MSO | META |
|---|---------|--------|--------|-----|------|
| 1 | +63 | PMS | `RESOLVE_INSTANT_APP_PACKAGE` | ✗ | ✗ |
| 2 | +279 | RoleSvc | `app.role.RoleControllerService` | ✗ | ✗ |
| 3 | +370 | IMMS | `view.InputMethod` | ✗ | ✓ |
| 4 | +374 | A11ySvc | `accessibilityservice.AccessibilityService` | ✗ | ✗ |
| 5 | +472 | WifiSvc | `settings.SETTINGS` | ✗ | ✗ |
| 6 | +530 | NMS | `service.notification.NotificationAssistantService` | ✗ | ✗ |
| 7 | +533 | NMS | `service.notification.ConditionProviderService` | ✗ | ✓ |
| 8 | +615 | CDM | `permission.PermissionControllerService` | ✗ | ✗ |

Total unique C2 violations: 8
  With GET_META_DATA: 2

## Unprotected Callers Breakdown

| Caller | Queries | With META_DATA | Distinct Intents |
|--------|---------|----------------|------------------|
| NMS | 6 | 5 | service.notification.ConditionProviderService, service.notification.Notificat... |
| PMS | 1 | 0 | RESOLVE_INSTANT_APP_PACKAGE |
| RoleSvc | 1 | 0 | app.role.RoleControllerService |
| IMMS | 1 | 1 | view.InputMethod |
| A11ySvc | 1 | 0 | accessibilityservice.AccessibilityService |
| WifiSvc | 1 | 0 | settings.SETTINGS |
| CDM | 1 | 0 | permission.PermissionControllerService |