-- ==========================================================
-- REVIA AI - PostgreSql & Supabase Database Schema
-- Includes full Row Level Security (RLS) policies
-- Slogan: "Apprends mieux. Révise plus vite."
-- ==========================================================

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Profiles table (tied to auth.users)
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    name TEXT NOT NULL DEFAULT 'Alex',
    email TEXT UNIQUE NOT NULL,
    plan TEXT NOT NULL DEFAULT 'FREE' CHECK (plan IN ('FREE', 'PRO', 'MAX')),
    credits INT NOT NULL DEFAULT 20,
    streak_days INT NOT NULL DEFAULT 5,
    study_time_minutes INT NOT NULL DEFAULT 120,
    mastered_concepts_count INT NOT NULL DEFAULT 18,
    needs_review_count INT NOT NULL DEFAULT 4,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE,
    is_dark_mode BOOLEAN NOT NULL DEFAULT TRUE,
    notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    language TEXT NOT NULL DEFAULT 'Français',
    subscription_renewal_date TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can view their own profile"
    ON public.profiles FOR SELECT
    USING (auth.uid() = id);

CREATE POLICY "Users can update their own profile"
    ON public.profiles FOR UPDATE
    USING (auth.uid() = id);

-- 2. Courses table
CREATE TABLE IF NOT EXISTS public.courses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    subject TEXT NOT NULL,
    level TEXT NOT NULL,
    description TEXT,
    raw_content TEXT NOT NULL,
    extracted_topics TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.courses ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can manage their own courses"
    ON public.courses FOR ALL
    USING (auth.uid() = user_id);

-- 3. Course Documents table (Storage reference)
CREATE TABLE IF NOT EXISTS public.course_documents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    course_id UUID NOT NULL REFERENCES public.courses(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    file_name TEXT NOT NULL,
    file_url TEXT NOT NULL,
    file_type TEXT NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.course_documents ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can manage their course documents"
    ON public.course_documents FOR ALL
    USING (auth.uid() = user_id);

-- 4. Revision Summaries table
CREATE TABLE IF NOT EXISTS public.summaries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    course_id UUID NOT NULL REFERENCES public.courses(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    course_title TEXT NOT NULL,
    summary_text TEXT NOT NULL,
    key_concepts TEXT NOT NULL,
    definitions TEXT NOT NULL,
    key_takeaways TEXT NOT NULL,
    examples TEXT NOT NULL,
    common_mistakes TEXT NOT NULL,
    mini_recap TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.summaries ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can manage their own summaries"
    ON public.summaries FOR ALL
    USING (auth.uid() = user_id);

-- 5. Quizzes table
CREATE TABLE IF NOT EXISTS public.quizzes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    course_id UUID NOT NULL REFERENCES public.courses(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    course_title TEXT NOT NULL,
    difficulty TEXT NOT NULL,
    question_count INT NOT NULL,
    quiz_type TEXT NOT NULL,
    questions_json JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.quizzes ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can manage their own quizzes"
    ON public.quizzes FOR ALL
    USING (auth.uid() = user_id);

-- 6. Quiz Results table
CREATE TABLE IF NOT EXISTS public.quiz_results (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    quiz_id UUID REFERENCES public.quizzes(id) ON DELETE SET NULL,
    course_id UUID NOT NULL REFERENCES public.courses(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    course_title TEXT NOT NULL,
    score INT NOT NULL,
    max_score INT NOT NULL DEFAULT 20,
    total_questions INT NOT NULL,
    percentage INT NOT NULL,
    correct_count INT NOT NULL,
    incorrect_count INT NOT NULL,
    review_recommendation TEXT NOT NULL,
    user_answers_json JSONB,
    completed_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.quiz_results ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can manage their quiz results"
    ON public.quiz_results FOR ALL
    USING (auth.uid() = user_id);

-- 7. Flashcards table
CREATE TABLE IF NOT EXISTS public.flashcards (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    course_id UUID NOT NULL REFERENCES public.courses(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    course_title TEXT NOT NULL,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    is_mastered BOOLEAN NOT NULL DEFAULT FALSE,
    review_count INT NOT NULL DEFAULT 0,
    last_reviewed_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.flashcards ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can manage their flashcards"
    ON public.flashcards FOR ALL
    USING (auth.uid() = user_id);

-- 8. Audio Lessons table
CREATE TABLE IF NOT EXISTS public.audio_lessons (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    course_id UUID NOT NULL REFERENCES public.courses(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    course_title TEXT NOT NULL,
    title TEXT NOT NULL,
    introduction TEXT NOT NULL,
    explanation TEXT NOT NULL,
    examples TEXT NOT NULL,
    recap TEXT NOT NULL,
    quick_questions TEXT NOT NULL,
    full_script TEXT NOT NULL,
    duration_seconds INT NOT NULL DEFAULT 180,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.audio_lessons ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can manage their audio lessons"
    ON public.audio_lessons FOR ALL
    USING (auth.uid() = user_id);

-- 9. Subscriptions table
CREATE TABLE IF NOT EXISTS public.subscriptions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    plan TEXT NOT NULL CHECK (plan IN ('FREE', 'PRO', 'MAX')),
    status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'CANCELLED', 'EXPIRED', 'TRIAL')),
    provider TEXT NOT NULL CHECK (provider IN ('PAYPAL', 'STRIPE', 'SYSTEM')),
    subscription_id TEXT,
    current_period_start TIMESTAMP WITH TIME ZONE NOT NULL,
    current_period_end TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.subscriptions ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can view their own subscriptions"
    ON public.subscriptions FOR SELECT
    USING (auth.uid() = user_id);

-- 10. Payments table
CREATE TABLE IF NOT EXISTS public.payments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    amount NUMERIC(10, 2) NOT NULL,
    currency TEXT NOT NULL DEFAULT 'EUR',
    status TEXT NOT NULL DEFAULT 'COMPLETED' CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED')),
    provider TEXT NOT NULL CHECK (provider IN ('PAYPAL', 'STRIPE')),
    transaction_id TEXT UNIQUE NOT NULL,
    plan_or_pack TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.payments ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can view their payments"
    ON public.payments FOR SELECT
    USING (auth.uid() = user_id);

-- 11. Credit Transactions table
CREATE TABLE IF NOT EXISTS public.credit_transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    amount INT NOT NULL,
    description TEXT NOT NULL,
    type TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

ALTER TABLE public.credit_transactions ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can view their credit transactions"
    ON public.credit_transactions FOR SELECT
    USING (auth.uid() = user_id);
