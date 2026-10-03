import React, { useState } from 'react';
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  SafeAreaView,
  StatusBar,
  TextInput,
  StyleSheet,
} from 'react-native';

// --- INTERFACES & TIPAGEM RIGOROSA ---

export interface Trail {
  name: string;
  progress: number;
  icon: string;
}

export interface Hero {
  id: string;
  name: string;
  role: string;
  profession: string;
  level: number;
  xp: number;
  gold: number;
  hp: number;
  mp: number;
  accent: string;
  trails: Trail[];
  relic: string;
  skills: string[];
}

export interface Quest {
  id: string;
  type: 'SOLO' | 'GUILDA' | 'BOSS';
  title: string;
  description: string;
  xp: number;
}

// --- MASSA DE DADOS (HERÓIS & QUESTS) ---

export const HEROES: Record<string, Hero> = {
  random: {
    id: 'random',
    name: 'Random',
    role: 'Explorador',
    profession: 'Aprendiz do Reino',
    level: 1,
    xp: 0,
    gold: 0,
    hp: 100,
    mp: 50,
    accent: '#00e5ff',
    relic: 'Amuleto do Primeiro Despertar',
    skills: ['Foco Racional', 'Curiosidade Investigativa'],
    trails: [
      { name: 'Matemática & Lógica', progress: 0, icon: '📐' },
      { name: 'Comunicação & Linguagens', progress: 0, icon: '📜' },
      { name: 'Ciências & Tecnologia', progress: 0, icon: '⚡' },
    ],
  },
  carlos: {
    id: 'carlos',
    name: 'Carlos',
    role: 'Druida',
    profession: 'Nutricionista',
    level: 14,
    xp: 8900,
    gold: 1450,
    hp: 850,
    mp: 620,
    accent: '#39ff14',
    relic: 'Cálice Botânico Ancestral',
    skills: ['Bioquímica Celular', 'Metabolismo Ativo', 'Equilíbrio Vital', 'Fitoterapia Avançada'],
    trails: [
      { name: 'Ciências Biológicas', progress: 95, icon: '🌿' },
      { name: 'Saúde & Nutrição', progress: 90, icon: '🥗' },
      { name: 'Pesquisa Científica', progress: 85, icon: '🔬' },
    ],
  },
  flavia: {
    id: 'flavia',
    name: 'Flávia',
    role: 'Negociadora',
    profession: 'Vendas & Negócios',
    level: 15,
    xp: 9800,
    gold: 3200,
    hp: 790,
    mp: 740,
    accent: '#ff073a',
    relic: 'Contrato de Ouro Rubro',
    skills: ['Persuasão Crítica', 'Leitura Comportamental', 'Pitch Irresistível', 'Fechamento Tático'],
    trails: [
      { name: 'Comunicação & Vendas', progress: 98, icon: '💎' },
      { name: 'Negociação Estratégica', progress: 92, icon: '🤝' },
      { name: 'Economia Comportamental', progress: 88, icon: '📊' },
    ],
  },
  kaio: {
    id: 'kaio',
    name: 'Kaio',
    role: 'Clérigo',
    profession: 'Dentista',
    level: 13,
    xp: 7600,
    gold: 1800,
    hp: 920,
    mp: 810,
    accent: '#00e5ff',
    relic: 'Espelho Odontológico Solar',
    skills: ['Anatomia Craniofacial', 'Precisão Cirúrgica', 'Assepsia Arcana', 'Restauração Estética'],
    trails: [
      { name: 'Saúde Oral & Anatomia', progress: 92, icon: '🦷' },
      { name: 'Biomedicina Preventiva', progress: 88, icon: '🧬' },
      { name: 'Cuidado Humano & Empatia', progress: 85, icon: '✨' },
    ],
  },
  victor: {
    id: 'victor',
    name: 'Victor',
    role: 'Paladino',
    profession: 'Advogado',
    level: 14,
    xp: 8400,
    gold: 2100,
    hp: 980,
    mp: 590,
    accent: '#ffd700',
    relic: 'Balança da Justiça Eterna',
    skills: ['Argumentação Jurídica', 'Retórica Forense', 'Oratória Imparável', 'Mediação de Conflitos'],
    trails: [
      { name: 'Direito & Cidadania', progress: 94, icon: '⚖️' },
      { name: 'Oratória & Lógica Formal', progress: 90, icon: '🏛️' },
      { name: 'Ética Pública & Compliance', progress: 86, icon: '🛡️' },
    ],
  },
  wagner: {
    id: 'wagner',
    name: 'Wagner',
    role: 'Artífice',
    profession: 'Robótica & Engenharia',
    level: 15,
    xp: 9500,
    gold: 2400,
    hp: 820,
    mp: 900,
    accent: '#bc13fe',
    relic: 'Núcleo Mecatrônico Quântico',
    skills: ['Algoritmos Embarcados', 'Cinemática Robótica', 'Automação IoT', 'Visão Computacional'],
    trails: [
      { name: 'Engenharia & Robótica', progress: 96, icon: '🤖' },
      { name: 'Programação de Sistemas', progress: 94, icon: '💻' },
      { name: 'Prototipagem 3D & Hardware', progress: 89, icon: '⚙️' },
    ],
  },
  willaydson: {
    id: 'willaydson',
    name: 'Willaydson',
    role: 'Estrategista',
    profession: 'Professor & Cientista',
    level: 15,
    xp: 9990,
    gold: 2900,
    hp: 880,
    mp: 980,
    accent: '#ff00ff',
    relic: 'Grimório CRISP-DM Pedagógico',
    skills: ['Metodologia CRISP-DM', 'Engenharia de Prompt', 'Arquitetura de Dados', 'Mentoria Adaptativa'],
    trails: [
      { name: 'Data Science & IA na Educação', progress: 99, icon: '🧠' },
      { name: 'Metodologias Ativas', progress: 95, icon: '📚' },
      { name: 'Liderança Pedagógica', progress: 92, icon: '🎯' },
    ],
  },
};

export const QUESTS: Quest[] = [
  {
    id: 'q1',
    type: 'SOLO',
    title: 'O Enigma dos Coeficientes',
    description: 'Identifique os termos A, B e C da equação quadrática para romper as correntes da distração mental.',
    xp: 250,
  },
  {
    id: 'q2',
    type: 'GUILDA',
    title: 'Hackathon CRISP-DM: Ciclo de Dados',
    description: 'Estruture junto à sua guilda as 6 etapas do CRISP-DM para resolver um problema real da comunidade escolar.',
    xp: 750,
  },
  {
    id: 'q3',
    type: 'BOSS',
    title: 'A Máquina da Ilusão (Brain Rot Cósmico)',
    description: 'Confronte o titã da desatenção e do vício algorítmico munido de puro pensamento crítico e método científico.',
    xp: 1500,
  },
  {
    id: 'q4',
    type: 'SOLO',
    title: 'Refinamento de Algoritmo de IA',
    description: 'Ajuste hiperparâmetros, audite vieses de treinamento e otimize a curva de convergência de uma rede neural.',
    xp: 350,
  },
  {
    id: 'q5',
    type: 'GUILDA',
    title: 'Expedição Multidisciplinar Integrada',
    description: 'Combine conhecimentos de saúde, robótica, oratória e vendas em uma solução empreendedora para o Ensino Médio.',
    xp: 900,
  },
];

export const CRISP_DM_STAGES = [
  {
    step: '01',
    name: 'Entendimento do Negócio',
    subtitle: 'Business Understanding',
    icon: '🎯',
    description: 'Mapeamento dos objetivos educacionais, critérios de sucesso e formulação de perguntas científicas com IA.',
  },
  {
    step: '02',
    name: 'Entendimento dos Dados',
    subtitle: 'Data Understanding',
    icon: '📊',
    description: 'Coleta de evidências, análise exploratória, verificação de qualidade dos dados dos estudantes e detecção de anomalias.',
  },
  {
    step: '03',
    name: 'Preparação dos Dados',
    subtitle: 'Data Preparation',
    icon: '🧹',
    description: 'Limpeza, normalização, enriquecimento de atributos e estruturação dos conjuntos de treino para os modelos.',
  },
  {
    step: '04',
    name: 'Modelagem & IA',
    subtitle: 'Modeling',
    icon: '🤖',
    description: 'Aplicação de algoritmos de aprendizado de máquina, redes neurais e modelos generativos ajustados ao contexto.',
  },
  {
    step: '05',
    name: 'Avaliação Crítica',
    subtitle: 'Evaluation',
    icon: '⚖️',
    description: 'Validação rigorosa contra os objetivos de negócio, avaliação ética, matriz de confusão e revisão pedagógica.',
  },
  {
    step: '06',
    name: 'Implementação em Campo',
    subtitle: 'Deployment',
    icon: '🚀',
    description: 'Integração na rotina da escola, monitoramento contínuo com dashboards e planos de sustentabilidade.',
  },
];

// --- COMPONENTE PRINCIPAL ---

export default function QuestIAApp() {
  const [userName, setUserName] = useState<string>('');
  const [isRegistered, setIsRegistered] = useState<boolean>(false);
  const [activeTab, setActiveTab] = useState<'home' | 'jogos' | 'mestre' | 'trilhas' | 'fundadores'>('home');
  const [currentHero, setCurrentHero] = useState<Hero>(HEROES.willaydson);

  // Regra de Negócio Dinâmica
  const getDisplayName = (hero: Hero): string => {
    if (hero.id === 'random') {
      const trimmed = userName.trim();
      return trimmed.length > 0 ? trimmed : 'Jogador';
    }
    return hero.name;
  };

  // --- 1. ONBOARDING SCREEN ---
  const renderOnboarding = () => (
    <SafeAreaView style={styles.onboardingContainer}>
      <StatusBar barStyle="light-content" backgroundColor="#090c15" />
      <View style={styles.onboardingCard}>
        <Text style={styles.neonLogo}>QUEST<Text style={{ color: '#00e5ff' }}>IA</Text></Text>
        <Text style={styles.onboardingSubtitle}>PLATAFORMA GAMIFICADA EDUCACIONAL</Text>
        <Text style={styles.onboardingDesc}>
          Adentre o Reino de QuestIA. Fortaleça seu arquétipo contra a Máquina da Ilusão, domine trilhas de conhecimento e desvende a metodologia CRISP-DM com IA.
        </Text>

        <View style={styles.inputWrapper}>
          <Text style={styles.inputLabel}>NOME DO NOVO AVENTUREIRO:</Text>
          <TextInput
            style={styles.textInput}
            placeholder="Digite o nome do seu herói..."
            placeholderTextColor="#5a688a"
            value={userName}
            onChangeText={setUserName}
            autoCapitalize="words"
          />
        </View>

        <TouchableOpacity
          style={[
            styles.enterButton,
            { backgroundColor: userName.trim().length > 0 ? '#00e5ff' : '#222d42' },
          ]}
          disabled={userName.trim().length === 0}
          onPress={() => {
            setIsRegistered(true);
            setCurrentHero(HEROES.random);
          }}
          activeOpacity={0.8}
        >
          <Text
            style={[
              styles.enterButtonText,
              { color: userName.trim().length > 0 ? '#090c15' : '#617194' },
            ]}
          >
            ENTRAR NO REINO ⚔️
          </Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );

  // --- 2. HOME SCREEN ---
  const renderHome = () => (
    <ScrollView style={styles.scrollContent} showsVerticalScrollIndicator={false}>
      {/* Header do Reino */}
      <View style={styles.topHeader}>
        <Text style={styles.topHeaderTitle}>REINO DE QUESTIA</Text>
        <Text style={styles.topHeaderSub}>ENSINO MÉDIO & METODOLOGIA CRISP-DM</Text>
      </View>

      {/* Hero Card */}
      <View style={[styles.heroCard, { borderColor: currentHero.accent }]}>
        <View style={styles.heroCardTop}>
          <View>
            <Text style={[styles.heroName, { color: currentHero.accent }]}>
              {getDisplayName(currentHero)}
            </Text>
            <Text style={styles.heroRole}>
              {currentHero.role.toUpperCase()} • {currentHero.profession}
            </Text>
          </View>
          <View style={[styles.levelBadge, { backgroundColor: currentHero.accent + '22', borderColor: currentHero.accent }]}>
            <Text style={[styles.levelBadgeText, { color: currentHero.accent }]}>
              NÍVEL {currentHero.level}
            </Text>
          </View>
        </View>

        {/* Stats Grid */}
        <View style={styles.statsGrid}>
          <View style={styles.statBox}>
            <Text style={styles.statLabel}>HP (VIDA)</Text>
            <Text style={[styles.statValue, { color: '#ff4757' }]}>{currentHero.hp}</Text>
          </View>
          <View style={styles.statBox}>
            <Text style={styles.statLabel}>MP (MANA)</Text>
            <Text style={[styles.statValue, { color: '#2ed573' }]}>{currentHero.mp}</Text>
          </View>
          <View style={styles.statBox}>
            <Text style={styles.statLabel}>OURO</Text>
            <Text style={[styles.statValue, { color: '#ffa502' }]}>{currentHero.gold} G</Text>
          </View>
          <View style={styles.statBox}>
            <Text style={styles.statLabel}>XP TOTAL</Text>
            <Text style={[styles.statValue, { color: currentHero.accent }]}>{currentHero.xp}</Text>
          </View>
        </View>

        {/* Botão de Missão Atual */}
        <TouchableOpacity
          style={[styles.primaryActionBtn, { backgroundColor: currentHero.accent }]}
          onPress={() => setActiveTab('jogos')}
          activeOpacity={0.85}
        >
          <Text style={styles.primaryActionBtnText}>CONTINUAR MISSÃO ATUAL ⚔️</Text>
        </TouchableOpacity>
      </View>

      {/* Relíquia do Herói */}
      <View style={styles.sectionContainer}>
        <Text style={styles.sectionTitle}>RELÍQUIA SAGRADA</Text>
        <View style={[styles.relicCard, { borderColor: currentHero.accent + '66' }]}>
          <Text style={styles.relicIcon}>🔮</Text>
          <View style={{ flex: 1 }}>
            <Text style={[styles.relicName, { color: currentHero.accent }]}>{currentHero.relic}</Text>
            <Text style={styles.relicDesc}>Canalizador de sabedoria ancestral e amplificador do foco reflexivo.</Text>
          </View>
        </View>
      </View>

      {/* Skills / Habilidades */}
      <View style={styles.sectionContainer}>
        <Text style={styles.sectionTitle}>HABILIDADES & ARQUÉTIPOS</Text>
        <View style={styles.pillsContainer}>
          {currentHero.skills.map((skill, index) => (
            <View
              key={index}
              style={[styles.pillBadge, { borderColor: currentHero.accent, backgroundColor: currentHero.accent + '15' }]}
            >
              <Text style={[styles.pillText, { color: currentHero.accent }]}>✦ {skill}</Text>
            </View>
          ))}
        </View>
      </View>

      {/* Atalho para Mestre CRISP-DM */}
      <TouchableOpacity
        style={[styles.crispBanner, { borderColor: currentHero.accent + '44' }]}
        onPress={() => setActiveTab('mestre')}
        activeOpacity={0.85}
      >
        <Text style={styles.crispBannerIcon}>🧠</Text>
        <View style={{ flex: 1 }}>
          <Text style={[styles.crispBannerTitle, { color: currentHero.accent }]}>METODOLOGIA CRISP-DM ATIVA</Text>
          <Text style={styles.crispBannerDesc}>Toque para auditar as 6 fases da ciência de dados geridas por IA.</Text>
        </View>
        <Text style={[styles.crispBannerArrow, { color: currentHero.accent }]}>➔</Text>
      </TouchableOpacity>

      <View style={{ height: 40 }} />
    </ScrollView>
  );

  // --- 3. QUESTS SCREEN ---
  const renderJogos = () => (
    <ScrollView style={styles.scrollContent} showsVerticalScrollIndicator={false}>
      <View style={styles.topHeader}>
        <Text style={styles.topHeaderTitle}>MURAL DE MISSÕES</Text>
        <Text style={styles.topHeaderSub}>DESAFIOS PEDAGÓGICOS GAMIFICADOS</Text>
      </View>

      {QUESTS.map((quest) => {
        const typeColor =
          quest.type === 'BOSS' ? '#ff4757' : quest.type === 'GUILDA' ? '#ffa502' : '#2ed573';

        return (
          <View key={quest.id} style={[styles.questCard, { borderColor: currentHero.accent + '55' }]}>
            <View style={styles.questHeader}>
              <View style={[styles.questTypeTag, { backgroundColor: typeColor + '25', borderColor: typeColor }]}>
                <Text style={[styles.questTypeText, { color: typeColor }]}>{quest.type}</Text>
              </View>
              <Text style={[styles.questXpBadge, { color: currentHero.accent }]}>+{quest.xp} XP</Text>
            </View>

            <Text style={styles.questTitle}>{quest.title}</Text>
            <Text style={styles.questDescription}>{quest.description}</Text>

            <TouchableOpacity
              style={[styles.questActionButton, { borderColor: currentHero.accent, backgroundColor: currentHero.accent + '20' }]}
              activeOpacity={0.8}
            >
              <Text style={[styles.questActionText, { color: currentHero.accent }]}>
                INICIAR DESAFIO ✦
              </Text>
            </TouchableOpacity>
          </View>
        );
      })}

      <View style={{ height: 40 }} />
    </ScrollView>
  );

  // --- 4. TRILHAS SCREEN ---
  const renderTrilhas = () => (
    <ScrollView style={styles.scrollContent} showsVerticalScrollIndicator={false}>
      <View style={styles.topHeader}>
        <Text style={styles.topHeaderTitle}>TRILHAS DE CONHECIMENTO</Text>
        <Text style={styles.topHeaderSub}>PROGRESSÃO CURRICULAR DO ARQUÉTIPO</Text>
      </View>

      <View style={styles.trailsWrapper}>
        {currentHero.trails.map((trail, index) => (
          <View key={index} style={styles.trailCard}>
            <View style={styles.trailHeader}>
              <Text style={styles.trailIcon}>{trail.icon}</Text>
              <View style={{ flex: 1 }}>
                <Text style={styles.trailName}>{trail.name}</Text>
                <Text style={styles.trailProgressText}>{trail.progress}% COMPLETO</Text>
              </View>
            </View>

            {/* Barra de Progresso Customizada */}
            <View style={styles.progressBarTrack}>
              <View
                style={[
                  styles.progressBarFill,
                  {
                    width: `${Math.max(5, trail.progress)}%`,
                    backgroundColor: currentHero.accent,
                  },
                ]}
              />
            </View>
          </View>
        ))}
      </View>

      <View style={{ height: 40 }} />
    </ScrollView>
  );

  // --- 5. MESTRE (CRISP-DM) SCREEN ---
  const renderMestre = () => (
    <ScrollView style={styles.scrollContent} showsVerticalScrollIndicator={false}>
      <View style={styles.topHeader}>
        <Text style={styles.topHeaderTitle}>PAINEL DO MESTRE</Text>
        <Text style={styles.topHeaderSub}>METODOLOGIA CRISP-DM GERENCIADA POR IA</Text>
      </View>

      <View style={styles.crispIntroCard}>
        <Text style={styles.crispIntroTitle}>CIÊNCIA DE DADOS APLICADA À EDUCAÇÃO</Text>
        <Text style={styles.crispIntroText}>
          O modelo CRISP-DM (Cross-Industry Standard Process for Data Mining) guia nossos alunos e professores na resolução estruturada de problemas com inteligência artificial.
        </Text>
      </View>

      {CRISP_DM_STAGES.map((stage) => (
        <View key={stage.step} style={[styles.crispStageCard, { borderColor: currentHero.accent + '44' }]}>
          <View style={styles.crispStageTop}>
            <View style={[styles.crispStepPill, { backgroundColor: currentHero.accent + '25', borderColor: currentHero.accent }]}>
              <Text style={[styles.crispStepNumber, { color: currentHero.accent }]}>ETAPA {stage.step}</Text>
            </View>
            <Text style={styles.crispStageIcon}>{stage.icon}</Text>
          </View>

          <Text style={styles.crispStageName}>{stage.name}</Text>
          <Text style={[styles.crispStageSubtitle, { color: currentHero.accent }]}>{stage.subtitle}</Text>
          <Text style={styles.crispStageDesc}>{stage.description}</Text>
        </View>
      ))}

      <View style={{ height: 40 }} />
    </ScrollView>
  );

  // --- 6. SELEÇÃO DE PERSONAGEM (FUNDADORES) ---
  const renderFundadores = () => (
    <ScrollView style={styles.scrollContent} showsVerticalScrollIndicator={false}>
      <View style={styles.topHeader}>
        <Text style={styles.topHeaderTitle}>SELEÇÃO DE ARQUÉTIPOS</Text>
        <Text style={styles.topHeaderSub}>ESCOLHA SEU HERÓI OU FUNDADOR</Text>
      </View>

      {Object.values(HEROES).map((hero) => {
        const isSelected = currentHero.id === hero.id;

        return (
          <TouchableOpacity
            key={hero.id}
            style={[
              styles.founderCard,
              {
                borderColor: isSelected ? hero.accent : '#222d42',
                borderWidth: isSelected ? 2 : 1,
                backgroundColor: isSelected ? '#1c2438' : '#151b2b',
              },
            ]}
            onPress={() => {
              setCurrentHero(hero);
              setActiveTab('home');
            }}
            activeOpacity={0.8}
          >
            <View style={styles.founderTop}>
              <View>
                <Text style={[styles.founderName, { color: hero.accent }]}>
                  {getDisplayName(hero)}
                </Text>
                <Text style={styles.founderProfession}>
                  {hero.role} • {hero.profession}
                </Text>
              </View>
              <View style={[styles.founderLevelBadge, { backgroundColor: hero.accent + '20', borderColor: hero.accent }]}>
                <Text style={[styles.founderLevelText, { color: hero.accent }]}>NV {hero.level}</Text>
              </View>
            </View>

            <Text style={styles.founderRelicText}>Relíquia: {hero.relic}</Text>

            <View style={styles.founderSkillsRow}>
              {hero.skills.slice(0, 3).map((skill, idx) => (
                <View key={idx} style={[styles.founderSkillPill, { borderColor: hero.accent + '55' }]}>
                  <Text style={[styles.founderSkillText, { color: hero.accent }]}>{skill}</Text>
                </View>
              ))}
            </View>

            {isSelected && (
              <View style={[styles.selectedBanner, { backgroundColor: hero.accent }]}>
                <Text style={styles.selectedBannerText}>✓ PERSONAGEM ATIVO</Text>
              </View>
            )}
          </TouchableOpacity>
        );
      })}

      <View style={{ height: 40 }} />
    </ScrollView>
  );

  // Se não passou pelo onboarding, bloqueia com a tela de registro
  if (!isRegistered) {
    return renderOnboarding();
  }

  return (
    <SafeAreaView style={styles.mainContainer}>
      <StatusBar barStyle="light-content" backgroundColor="#090c15" />

      {/* Conteúdo Dinâmico por Aba */}
      <View style={styles.screenContainer}>
        {activeTab === 'home' && renderHome()}
        {activeTab === 'jogos' && renderJogos()}
        {activeTab === 'mestre' && renderMestre()}
        {activeTab === 'trilhas' && renderTrilhas()}
        {activeTab === 'fundadores' && renderFundadores()}
      </View>

      {/* Bottom Navigation Fixa (5 Botões Táteis) */}
      <View style={styles.bottomNav}>
        {[
          { tab: 'home', label: 'HOME', icon: '🏛️' },
          { tab: 'jogos', label: 'QUESTS', icon: '⚔️' },
          { tab: 'mestre', label: 'MESTRE', icon: '🧠' },
          { tab: 'trilhas', label: 'TRILHAS', icon: '📜' },
          { tab: 'fundadores', label: 'HERÓIS', icon: '👥' },
        ].map((item) => {
          const isActive = activeTab === item.tab;
          const activeColor = currentHero.accent;

          return (
            <TouchableOpacity
              key={item.tab}
              style={styles.navButton}
              onPress={() => setActiveTab(item.tab as any)}
              activeOpacity={0.7}
            >
              <Text style={[styles.navIcon, { opacity: isActive ? 1 : 0.4 }]}>{item.icon}</Text>
              <Text
                style={[
                  styles.navLabel,
                  {
                    color: isActive ? activeColor : '#5a688a',
                    fontWeight: isActive ? '900' : '600',
                  },
                ]}
              >
                {item.label}
              </Text>
              {isActive && (
                <View style={[styles.activeIndicator, { backgroundColor: activeColor }]} />
              )}
            </TouchableOpacity>
          );
        })}
      </View>
    </SafeAreaView>
  );
}

// --- DESIGN SYSTEM & ESTILOS (CYBERPUNK / RPG GAMER DARK) ---

const styles = StyleSheet.create({
  mainContainer: {
    flex: 1,
    backgroundColor: '#090c15',
  },
  screenContainer: {
    flex: 1,
    backgroundColor: '#090c15',
  },
  scrollContent: {
    flex: 1,
    paddingHorizontal: 16,
    paddingTop: 12,
  },
  topHeader: {
    alignItems: 'center',
    marginBottom: 16,
    paddingVertical: 8,
  },
  topHeaderTitle: {
    fontSize: 20,
    fontWeight: '900',
    color: '#ffffff',
    letterSpacing: 2,
  },
  topHeaderSub: {
    fontSize: 10,
    fontWeight: '700',
    color: '#5a688a',
    letterSpacing: 1.5,
    marginTop: 3,
  },

  // ONBOARDING
  onboardingContainer: {
    flex: 1,
    backgroundColor: '#090c15',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  onboardingCard: {
    width: '100%',
    backgroundColor: '#151b2b',
    borderRadius: 16,
    borderWidth: 1.5,
    borderColor: '#00e5ff',
    padding: 24,
    alignItems: 'center',
    shadowColor: '#00e5ff',
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.25,
    shadowRadius: 12,
  },
  neonLogo: {
    fontSize: 34,
    fontWeight: '900',
    color: '#ffffff',
    letterSpacing: 3,
  },
  onboardingSubtitle: {
    fontSize: 11,
    fontWeight: '800',
    color: '#00e5ff',
    letterSpacing: 1.5,
    marginTop: 4,
    textAlign: 'center',
  },
  onboardingDesc: {
    fontSize: 13,
    color: '#9aa8c7',
    textAlign: 'center',
    lineHeight: 18,
    marginTop: 14,
    marginBottom: 24,
  },
  inputWrapper: {
    width: '100%',
    marginBottom: 20,
  },
  inputLabel: {
    fontSize: 10,
    fontWeight: '900',
    color: '#00e5ff',
    letterSpacing: 1,
    marginBottom: 6,
  },
  textInput: {
    width: '100%',
    height: 50,
    backgroundColor: '#090c15',
    borderWidth: 1,
    borderColor: '#2d3b58',
    borderRadius: 10,
    paddingHorizontal: 14,
    color: '#ffffff',
    fontSize: 15,
    fontWeight: '700',
  },
  enterButton: {
    width: '100%',
    height: 52,
    borderRadius: 10,
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#00e5ff',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
  },
  enterButtonText: {
    fontSize: 14,
    fontWeight: '900',
    letterSpacing: 1.5,
  },

  // HERO CARD (HOME)
  heroCard: {
    backgroundColor: '#151b2b',
    borderRadius: 16,
    borderWidth: 1.5,
    padding: 18,
    marginBottom: 16,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.2,
    shadowRadius: 10,
  },
  heroCardTop: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: 16,
  },
  heroName: {
    fontSize: 22,
    fontWeight: '900',
    letterSpacing: 1,
  },
  heroRole: {
    fontSize: 11,
    fontWeight: '700',
    color: '#9aa8c7',
    marginTop: 2,
    letterSpacing: 0.5,
  },
  levelBadge: {
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 8,
    borderWidth: 1,
  },
  levelBadgeText: {
    fontSize: 11,
    fontWeight: '900',
    letterSpacing: 1,
  },
  statsGrid: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 18,
  },
  statBox: {
    flex: 1,
    backgroundColor: '#090c15',
    borderRadius: 10,
    padding: 10,
    alignItems: 'center',
    marginHorizontal: 3,
    borderWidth: 1,
    borderColor: '#222d42',
  },
  statLabel: {
    fontSize: 9,
    fontWeight: '900',
    color: '#5a688a',
    letterSpacing: 0.5,
  },
  statValue: {
    fontSize: 14,
    fontWeight: '900',
    marginTop: 4,
  },
  primaryActionBtn: {
    height: 48,
    borderRadius: 10,
    justifyContent: 'center',
    alignItems: 'center',
  },
  primaryActionBtnText: {
    color: '#090c15',
    fontSize: 13,
    fontWeight: '900',
    letterSpacing: 1,
  },

  // RELÍQUIA
  sectionContainer: {
    marginBottom: 18,
  },
  sectionTitle: {
    fontSize: 11,
    fontWeight: '900',
    color: '#ffffff',
    letterSpacing: 1.5,
    marginBottom: 10,
  },
  relicCard: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#151b2b',
    borderRadius: 12,
    borderWidth: 1,
    padding: 14,
  },
  relicIcon: {
    fontSize: 28,
    marginRight: 12,
  },
  relicName: {
    fontSize: 14,
    fontWeight: '900',
    marginBottom: 2,
  },
  relicDesc: {
    fontSize: 11,
    color: '#9aa8c7',
    lineHeight: 15,
  },

  // SKILLS PILLS
  pillsContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  pillBadge: {
    paddingHorizontal: 12,
    paddingVertical: 7,
    borderRadius: 20,
    borderWidth: 1,
  },
  pillText: {
    fontSize: 11,
    fontWeight: '800',
    letterSpacing: 0.5,
  },

  // CRISP BANNER (HOME)
  crispBanner: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#151b2b',
    borderRadius: 12,
    borderWidth: 1,
    padding: 14,
    marginBottom: 10,
  },
  crispBannerIcon: {
    fontSize: 26,
    marginRight: 12,
  },
  crispBannerTitle: {
    fontSize: 12,
    fontWeight: '900',
    letterSpacing: 1,
  },
  crispBannerDesc: {
    fontSize: 10,
    color: '#9aa8c7',
    marginTop: 2,
  },
  crispBannerArrow: {
    fontSize: 18,
    fontWeight: '900',
    marginLeft: 8,
  },

  // QUESTS LIST
  questCard: {
    backgroundColor: '#151b2b',
    borderRadius: 14,
    borderWidth: 1,
    padding: 16,
    marginBottom: 14,
  },
  questHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  questTypeTag: {
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 6,
    borderWidth: 1,
  },
  questTypeText: {
    fontSize: 10,
    fontWeight: '900',
    letterSpacing: 1,
  },
  questXpBadge: {
    fontSize: 12,
    fontWeight: '900',
  },
  questTitle: {
    fontSize: 15,
    fontWeight: '900',
    color: '#ffffff',
    marginBottom: 6,
  },
  questDescription: {
    fontSize: 12,
    color: '#9aa8c7',
    lineHeight: 17,
    marginBottom: 14,
  },
  questActionButton: {
    height: 42,
    borderRadius: 8,
    borderWidth: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  questActionText: {
    fontSize: 11,
    fontWeight: '900',
    letterSpacing: 1,
  },

  // TRILHAS
  trailsWrapper: {
    gap: 12,
  },
  trailCard: {
    backgroundColor: '#151b2b',
    borderRadius: 14,
    borderWidth: 1,
    borderColor: '#222d42',
    padding: 16,
  },
  trailHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
  },
  trailIcon: {
    fontSize: 24,
    marginRight: 12,
  },
  trailName: {
    fontSize: 14,
    fontWeight: '900',
    color: '#ffffff',
  },
  trailProgressText: {
    fontSize: 10,
    fontWeight: '700',
    color: '#5a688a',
    marginTop: 2,
    letterSpacing: 0.5,
  },
  progressBarTrack: {
    width: '100%',
    height: 8,
    backgroundColor: '#090c15',
    borderRadius: 4,
    overflow: 'hidden',
  },
  progressBarFill: {
    height: '100%',
    borderRadius: 4,
  },

  // MESTRE (CRISP-DM)
  crispIntroCard: {
    backgroundColor: '#151b2b',
    borderRadius: 14,
    borderWidth: 1,
    borderColor: '#222d42',
    padding: 16,
    marginBottom: 16,
  },
  crispIntroTitle: {
    fontSize: 12,
    fontWeight: '900',
    color: '#ffffff',
    letterSpacing: 1,
    marginBottom: 6,
  },
  crispIntroText: {
    fontSize: 12,
    color: '#9aa8c7',
    lineHeight: 17,
  },
  crispStageCard: {
    backgroundColor: '#151b2b',
    borderRadius: 14,
    borderWidth: 1,
    padding: 16,
    marginBottom: 12,
  },
  crispStageTop: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  crispStepPill: {
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 6,
    borderWidth: 1,
  },
  crispStepNumber: {
    fontSize: 10,
    fontWeight: '900',
    letterSpacing: 1,
  },
  crispStageIcon: {
    fontSize: 20,
  },
  crispStageName: {
    fontSize: 15,
    fontWeight: '900',
    color: '#ffffff',
  },
  crispStageSubtitle: {
    fontSize: 11,
    fontWeight: '800',
    letterSpacing: 0.5,
    marginTop: 2,
    marginBottom: 6,
  },
  crispStageDesc: {
    fontSize: 11,
    color: '#9aa8c7',
    lineHeight: 16,
  },

  // SELEÇÃO DE FUNDADORES
  founderCard: {
    borderRadius: 14,
    padding: 16,
    marginBottom: 14,
  },
  founderTop: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: 8,
  },
  founderName: {
    fontSize: 17,
    fontWeight: '900',
    letterSpacing: 0.5,
  },
  founderProfession: {
    fontSize: 11,
    color: '#9aa8c7',
    marginTop: 2,
  },
  founderLevelBadge: {
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 6,
    borderWidth: 1,
  },
  founderLevelText: {
    fontSize: 10,
    fontWeight: '900',
    letterSpacing: 0.5,
  },
  founderRelicText: {
    fontSize: 11,
    color: '#7182a6',
    marginBottom: 10,
    fontStyle: 'italic',
  },
  founderSkillsRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 6,
  },
  founderSkillPill: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
    borderWidth: 1,
    backgroundColor: '#090c15',
  },
  founderSkillText: {
    fontSize: 10,
    fontWeight: '700',
  },
  selectedBanner: {
    marginTop: 12,
    paddingVertical: 4,
    borderRadius: 6,
    alignItems: 'center',
  },
  selectedBannerText: {
    color: '#090c15',
    fontSize: 10,
    fontWeight: '900',
    letterSpacing: 1,
  },

  // BOTTOM NAVIGATION
  bottomNav: {
    flexDirection: 'row',
    height: 64,
    backgroundColor: '#090c15',
    borderTopWidth: 1,
    borderTopColor: '#1d263b',
    paddingBottom: 4,
  },
  navButton: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    position: 'relative',
  },
  navIcon: {
    fontSize: 18,
    marginBottom: 2,
  },
  navLabel: {
    fontSize: 9,
    letterSpacing: 0.5,
  },
  activeIndicator: {
    position: 'absolute',
    bottom: 2,
    width: 18,
    height: 2,
    borderRadius: 1,
  },
});
