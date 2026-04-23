# API Token Setup

This project uses the IPinfo.io API for geolocation lookups. An API token is not required, but reliability
may be improved if you include one. You can get one for free.

## Getting an API Token

1. Visit [https://ipinfo.io/](https://ipinfo.io/)
2. Sign up for a free account
3. Get your free API token (free tier provides 50,000 requests per month)

## Setting up the API Token

The API token should be stored in `local.properties` file (which is gitignored):

```properties
API_TOKEN=your_actual_api_token_here
```

The token is **NOT** required for development - the app will work with IPinfo's free tier without a token, but with limited functionality.

**Important:** Never commit your API token to version control!
