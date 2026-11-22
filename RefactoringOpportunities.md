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
| **`Move::processEnemyFire`** | Long Method | **Extract Method** | 129829 |
| **`Scoreboard::showScoreboard`** | Long Method | **Single Return Point** | 129829 |
| **`Carrack`** | Duplicated Code | **Extract Superclass** | 129829 |
| **`Ship`** | Magic Strings / Speculative Generality | **Safe Delete & Constants Extraction** | 129829 |
| **`Move::processEnemyFire`** | Cryptic Parameter Name / Boolean Flag | **Introduce Parameter Object** | 129829 |
| `PdfExporter::exportarJogadas` | Long Method | Extract Method | 129841 |
| `PdfExporter::exportarJogadas` | Magic Numbers | Introduce Constant | 129841 |
| `Position::isAdjacentTo` | Complex Expression | Introduce Variable | 129841 |
| `Ship::buildShip` | Mysterious Name | Rename | 129841 |
| `Position::randomPosition` | Unnecessary Temporary Variable | Inline Variable | 129841 |