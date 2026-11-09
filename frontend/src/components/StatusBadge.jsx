const LABELS = {
  INTRODUZIDO: ["Pendente", "badge-pending"],
  EM_ANALISE: ["Em análise", "badge-pending"],
  APROVADO: ["Aprovado", "badge-approved"],
  REJEITADO: ["Rejeitado", "badge-rejected"],
  EM_EXECUCAO: ["Em execução", "badge-info"],
  CONCLUIDO: ["Concluído", "badge-info"],
  CANCELADO: ["Cancelado", "badge-neutral"],
};

export default function StatusBadge({ status }) {
  const [label, className] = LABELS[status] || [status, "badge-neutral"];
  return <span className={`badge ${className}`}>{label}</span>;
}
