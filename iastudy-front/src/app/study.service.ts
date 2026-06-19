import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface Flashcard {
  question: string;
  answer: string;
  isOpen?: boolean;
}

export interface StudyResponse {
  summary: string;
  flashcards: Flashcard[];
}

@Injectable({ providedIn: 'root' })
export class StudyService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/study/analyze';

  analyze(text: string, quantity: number) {
    return this.http.post<StudyResponse>(this.apiUrl, { text, quantity });
  }
}
