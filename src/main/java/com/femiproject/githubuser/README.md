# GitHub User Activity

## Requirements

- The application should run from the command line, accept the GitHub username as an argument, fetch the user’s recent activity using the GitHub API, and display it in the terminal. The user should be able to:

Provide the GitHub username as an argument when running the CLI.

```java
com.femiproject.githubuser.Main <username>
```

Fetch the recent activity of the specified GitHub user using the GitHub API. You can use the following endpoint to fetch the user’s activity:

- https://api.github.com/users/<username>/events
- Example: https://api.github.com/users/kamranahmedse/events

Display the fetched activity in the terminal.

- Output:
- - Pushed 3 commits to kamranahmedse/developer-roadmap
- Opened a new issue in kamranahmedse/developer-roadmap
- Starred kamranahmedse/developer-roadmap
- ...
