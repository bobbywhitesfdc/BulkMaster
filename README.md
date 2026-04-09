# BulkMaster
Bulk Loading Utility for Salesforce

> **GitHub:** [https://github.com/bobbywhitesfdc/BulkMaster](https://github.com/bobbywhitesfdc/BulkMaster)

<p>This is a Mavenized Java 8 project, so building it is simple:</p>
<p><code>mvn clean package</code></p>

---

## ⚠️ Security Notice: Credential-Based Authentication No Longer Works

**The legacy positional `{username} {password}` arguments and the `-un` / `-pwd` / `-loginurl` flags are deprecated and will be removed in a future release.**

**If your invocations look like this, they will fail or are at risk of failing:**

```bash
# ❌ Legacy — broken on orgs created Summer '23 or later, and on any org
#    where an admin has applied Salesforce's recommended security posture
java -jar BulkMaster-1.0-jar-with-dependencies.jar myuser@myorg.com MyPassword https://test.salesforce.com -o Lead -purge "..."
```

Salesforce has made two platform-level security changes that break this pattern. The failure mode is silent and confusing: the CLI returns `invalid_grant: authentication failure` even when your credentials are completely correct. [See why below.](#-why-credential-based-authentication-is-being-retired)

**Use `./scripts/bulkrunner.sh` instead:**

```bash
# ✅ Recommended — authenticates via SF CLI org alias, no credentials in the command line
./scripts/bulkrunner.sh my-sandbox -o Lead -purge "Select * From Lead Where status='Converted'" -D ./output -p 60
```

---

## Recommended: Use `bulkrunner.sh` with Salesforce CLI

The `./scripts/bulkrunner.sh` wrapper script eliminates credential exposure entirely by delegating authentication to the **Salesforce CLI** (`sf`). It resolves an org alias to a live access token and instance URL, then passes those directly to BulkMaster via `-authtoken` and `-instanceurl`. No username, no password, no Connected App OAuth configuration required beyond your existing CLI authentication.

### Prerequisites

- [Salesforce CLI](https://developer.salesforce.com/tools/salesforcecli) (`sf`) installed and on `$PATH`
- [`jq`](https://jqlang.github.io/jq/) installed and on `$PATH` (available via `brew install jq` on macOS)
- Java on `$PATH`
- BulkMaster built: `mvn clean package`
- At least one org authenticated: `sf org login web --alias my-org` or `sf org login jwt ...`

### Usage

```bash
./scripts/bulkrunner.sh <sf-alias> [BulkMaster flags...]
```

The `<sf-alias>` is the org alias you assigned when authenticating with `sf`. All remaining arguments are passed through verbatim to BulkMaster.

### Examples

**One-step Purge in a Sandbox**
```bash
./scripts/bulkrunner.sh my-sandbox -o Lead -purge "Select * From Lead Where status='Converted'" -D ./output -p 60
```

**List Jobs**
```bash
./scripts/bulkrunner.sh my-sandbox -l
```

**Execute a Query — poll every 10 seconds, download results to `./output`**
```bash
./scripts/bulkrunner.sh my-sandbox -o Lead -q "Select * From Lead Where status='New'" -D ./output -p 10
```

**Poll for Results on a Running Job**
```bash
./scripts/bulkrunner.sh my-sandbox -r -j 7500U000002UO1V -D ./output -p 10
```

**Uber Upsert — collection of files for a single object**
```bash
./scripts/bulkrunner.sh my-sandbox -o Account -x MyExternalId__c -uu ./input/AccountData -D ./output/AccountResults -p 60
```

---

## Direct Invocation (Token-Based)

If you are invoking BulkMaster outside of the shell wrapper — for example from a CI/CD pipeline — obtain your access token and instance URL via the CLI and pass them explicitly:

```bash
ACCESS_TOKEN=$(sf org display --target-org my-sandbox --json | jq -r '.result.accessToken')
INSTANCE_URL=$(sf org display --target-org my-sandbox --json | jq -r '.result.instanceUrl')

java -jar target/BulkMaster-1.0-jar-with-dependencies.jar \
  -authtoken "$ACCESS_TOKEN" \
  -instanceurl "$INSTANCE_URL" \
  [additional flags...]
```

---

## Build

```bash
mvn clean package
```

The output jar is `target/BulkMaster-1.0-jar-with-dependencies.jar`.

---

## Full Flag Reference

```
Flags:
-l     List Jobs
-i     Insert Records
-u     Upsert Records
-d     Delete Records
-purge One step command that queries and purges
-s     Get Job Status
-c     Close Job
-a     Abort Job
-r     Get Job results
-j     Hex ID of the Job
-f     Input filename
-D     Output Directory
-x     External ID fieldname for Upsert
-o     Object name for Insert, Update, or Delete
-p     Poll for results - interval in seconds
-q     SOQL Query string
-pk    Enable PK Chunking for Large Queries
-mx    Max records per job
-authtoken    Auth Token                          <- preferred authentication
-instanceurl  Instance URL used for API calls     <- preferred authentication
-apiversion   API Version xx.0

Deprecated (will be removed):
-loginurl     Login URL for Username-Password flow
-un           Username
-pwd          Password
```

> **Note:** The legacy positional arguments `{username} {password} [loginURL]` are also deprecated and will be removed alongside `-un`, `-pwd`, and `-loginurl`.

---

## 📋 Why Credential-Based Authentication Is Being Retired

Salesforce has made two significant, overlapping platform security changes that make passing credentials on the command line both increasingly unreliable and a security anti-pattern. Both changes are intentional, permanent, and progressively more restrictive over time.

### 1. OAuth 2.0 Username-Password Flow — Blocked by Default

Salesforce began blocking the OAuth 2.0 Username-Password flow by default for all orgs created with the **Summer '23 release or later**. For existing orgs, admins are strongly encouraged to disable it explicitly via *Setup → OAuth and OpenID Connect Settings*. The flow is formally classified as a "special scenario" — a last resort — and Salesforce recommends replacing it with **Client Credentials** or **JWT Bearer** flows for server-to-server integrations.

The reasons this flow is considered insecure are well-documented:
- Credentials (username + password + security token) travel in the POST body and may be logged.
- A stolen credential pair allows full impersonation with no scope restriction.
- No refresh token is issued, so the integration cannot be discovered or revoked via the OAuth token list.
- It bypasses MFA entirely.

**References:**
- Salesforce Release Notes — [OAuth 2.0 Username-Password Flow Blocked by Default](https://help.salesforce.com/s/articleView?id=release-notes.rn_security_username-password_flow_blocked_by_default.htm&release=244&type=5) (Spring '24 / Release 244)
- Salesforce Help — [OAuth 2.0 Username-Password Flow for Special Scenarios](https://help.salesforce.com/s/articleView?id=xcloud.remoteaccess_oauth_username_password_flow.htm&type=5)
- Salesforce Release Notes — [Block the OAuth 2.0 Username-Password Flow at an Org Level](https://help.salesforce.com/s/articleView?id=release-notes.rn_security_disable_username_password_flow.htm&release=238&type=5) (Summer '23 / Release 238)

### 2. Connected App "All Users May Self-Authorize" (Global Scope) — Restricted

Starting **September 2025**, Salesforce fundamentally changed the Connected App authorization model. The legacy default — *All users may self-authorize* — which permitted any authenticated user to grant a Connected App access to the org without admin involvement, is no longer the operative behavior for uninstalled apps. This change was driven directly by a series of social engineering (vishing) campaigns in which threat actors impersonated IT staff and convinced users to authorize malicious data-extraction apps — a pattern exploited against organizations including Air France and KLM.

Key points of the September 2025 change:
- Uninstalled Connected Apps are now **blocked by default** for standard users.
- The **OAuth 2.0 Device Flow** was eliminated entirely with no exceptions; all existing Device Flow sessions were immediately terminated.
- Two new permissions gate access for trusted users: `Approve Uninstalled Connected Apps` (Summer '25) and `Use Any API Client`.
- Salesforce recommends that all Connected Apps be formally installed and scoped to *Admin-approved users are pre-authorized* rather than global self-authorization.

**References:**
- Salesforce Admins Blog — [Get Ready: Changes to Connected App Usage Restrictions Coming This September](https://admin.salesforce.com/blog/2025/get-ready-for-changes-to-connected-app-usage-restrictions) (August 2025)
- Salesforce Help — [Prepare for Connected App Usage Restrictions Change](https://help.salesforce.com/s/articleView?id=005132365&language=en_US&type=1) (Article 005132365)
- Salesforce Ben — [Salesforce Hardens Connected Apps Security Amid Social Engineering Attacks](https://www.salesforceben.com/salesforce-hardens-connected-apps-security-amid-social-engineering-attacks/) (August 2025)