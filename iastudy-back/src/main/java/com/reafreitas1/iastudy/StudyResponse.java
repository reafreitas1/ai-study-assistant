package com.reafreitas1.iastudy;
import java.util.List;

public record StudyResponse(List<String> summary, List<Flashcard> flashcards) {}