# Refactoring Opportunities

| Local | Nome do cheiro no código | Nome da refabricação | Número de aluno |
| :--- | :--- | :--- | :--- |
| `Game::randomEnemyFire` | Long Method | Extract Method | 129870 |
| `Game::fireSingleShot` | Complex Expression | Extract Variable | 129870 |
| `GameTimer::endTurn` | Magic Numbers | Introduce Constant | 129870 |
| `Frigate` | Duplicated Code | Pull Members Up | 129870 |
| `Scoreboard::showScoreboard` | Unnecessary Temporary Variable | Inline Variable | 129870 |
| `Internacionalizacao::calcularNumeros`, `VALOR_LIMITE_PONTOS` | Dead Code / Speculative Generality | Safe Delete | 129849 |
| `Fleet::addShip` (parâmetro `s`) | Mysterious Name | Rename | 129849 |
| `Fleet::colisionRisk` | Negated Conditional (usado como `!colisionRisk(s)`) | Invert Boolean | 129849 |
| `Fleet::printShips` | Feature Envy (MethodMayBeStatic) | Make Static | 129849 |
| `Fleet::printShips` (parâmetro `ships` esconde o atributo) | Confusing Parameter Name | Change Signature | 129849 |
| `IFleet::FLEET_SIZE` | Unnecessary Boxing (`Integer` em vez de `int`) | Type Migration | 129849 |
| `Move::isValid` | Magic Number | Introduce Constant | 129829 |
| `Scoreboard::updateScore` | Long Method | Extract Method | 129829 |
| `Carrack::armor` | Public Field | Encapsulate Field | 129829 |
| `Battleship` | Unused Private Field | Safe Delete | 129829 |
| `Move` | Literal repetition | Extract Constant | 129829 |
| `Game::initializeGame` | Complex Condition | Decompose Conditional | 129829 |
| `BattleshipGamePlayer` (parâmetro `x`) | Cryptic Parameter Name | Rename | 129829 |
