Feature: Word mapper

  Mapping api model to ui model

  Scenario: Convert WordInfo to WordItem
    Given A WordInfo with below data:
      | word    | definition                                             | phonetic |
      | "hello" | "used as a greeting or to begin a phone conversation." | "həˈləʊ" |
    When Convert a WordInfo to WordItem
    Then The WordItem content is correct
