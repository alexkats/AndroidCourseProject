# Development Policy
Here is located some kind of basic development policy for developers of this game.

## Workflow
1. If you want to improve or fix something, first of all create a **TODO** card in Project "Space Arcade" tab. Then, when you are sure and totally know, what should be done under this card, you need confirm this card and convert it to issue. Issue name must begin with **[SA-X]**, where **X** is next issue number after the last one in issues.
2. After these steps you should move card from **Confirmed** to **In Progress** area and create a branch from **master** which matches your issue. Branch name should be **bugfix/SA-X** if issue is a bug or **feature/SA-X** otherwise. **X** in branch name should match **X** in issue name.
3. All commit messages should start with exact copy of issue name, after it you can write something meaningful about your commit, but don't have to.
4. Now you can start development process in your branch. When you are finished, create a pull request with name as in issue and description **Fixes #y**, where **y** is a issue number given by github. Alse move card to **Reviewing** area.
5. After these steps you should wait for another developer to review your code and approve it. Once approved, you can merge your changes to **master** branch. After this issue and pull request would be closed automatically. Finally, you should move card to **Resolved** area.

Please, keep in mind, that forced push to **master** is forbidden.
