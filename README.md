# VetManager

Sistema desktop para gerenciamento básico de uma clínica veterinária, desenvolvido em Java Swing como projeto acadêmico de Engenharia de Software.

## Sobre o projeto

O VetManager tem como objetivo facilitar o gerenciamento de clientes, animais e consultas veterinárias por meio de uma interface gráfica desktop integrada a um banco de dados PostgreSQL.

O projeto aplica conceitos de programação orientada a objetos, interfaces gráficas, persistência de dados e separação de responsabilidades.

## Funcionalidades

* **Autenticação:** login de usuários.
* **Clientes:** cadastro, consulta, edição e exclusão de clientes.
* **Animais:** cadastro e gerenciamento de animais vinculados aos seus responsáveis.
* **Consultas:** cadastro e gerenciamento de consultas vinculadas aos animais.
* **Interface gráfica:** telas desenvolvidas com Java Swing.
* **Tabelas:** visualização e seleção de registros utilizando `JTable`.
* **Persistência:** armazenamento e recuperação de dados em PostgreSQL.

## Tecnologias

* Java
* Java Swing
* JDBC
* PostgreSQL
* Git

## Arquitetura

O projeto utiliza uma organização em camadas para separar a interface gráfica, as regras de manipulação dos dados e o acesso ao banco de dados.

```text
src/
└── br/com/sistema/
    ├── dao/
    │   ├── GenericDAO.java
    │   ├── UserDAO.java
    │   ├── ClientDAO.java
    │   ├── AnimalDAO.java
    │   └── AppointmentDAO.java
    ├── model/
    │   ├── User.java
    │   ├── Client.java
    │   ├── Animal.java
    │   └── Appointment.java
    ├── view/
    │   ├── LoginFrame.java
    │   ├── MainFrame.java
    │   ├── ClientFrame.java
    │   ├── AnimalFrame.java
    │   └── AppointmentFrame.java
    ├── jdbc/
    │   └── ConnectionFactory.java
    └── main/
        └── Main.java
```

*Os nomes dos arquivos acima representam a organização prevista; ajuste a árvore para corresponder exatamente aos arquivos presentes no repositório.*

### Responsabilidades

* **View:** apresenta as telas e recebe as interações do usuário.
* **Model:** representa as entidades do domínio.
* **DAO:** encapsula as operações de persistência e consulta.
* **JDBC:** gerencia a conexão com o banco de dados.
* **Main:** inicializa a aplicação.

## Modelo de dados

O banco de dados utiliza as seguintes entidades:

| Tabela         | Descrição                   |
| -------------- | --------------------------- |
| `users`        | Usuários do sistema         |
| `clients`      | Clientes da clínica         |
| `animals`      | Animais e seus responsáveis |
| `appointments` | Consultas veterinárias      |

### Relacionamentos

* Um cliente pode possuir vários animais.
* Um animal pode possuir várias consultas.
* Cada animal deve estar vinculado a um cliente.
* Cada consulta deve estar vinculada a um animal.

## Como executar

### Pré-requisitos

* JDK compatível com o projeto.
* PostgreSQL instalado e em execução.
* IDE Java, como Apache NetBeans ou IntelliJ IDEA.
* Driver JDBC do PostgreSQL.

### 1. Clone o repositório

```bash
git clone <REPOSITORY_URL>
cd <REPOSITORY_DIRECTORY>
```

Substitua os valores pelos dados reais do repositório.

### 2. Configure o banco de dados

Crie o banco de dados e as tabelas necessárias conforme o esquema SQL do projeto.

### 3. Configure a conexão

Ajuste a classe `ConnectionFactory` com as informações de conexão do PostgreSQL, incluindo endereço, porta, banco de dados, usuário e senha.

Evite versionar credenciais reais no repositório.

### 4. Execute a aplicação

Abra o projeto na IDE, verifique as dependências e execute a classe principal `Main.java`.

## Objetivos acadêmicos

O desenvolvimento do VetManager permite aplicar os seguintes conceitos:

* Programação orientada a objetos.
* Desenvolvimento de interfaces gráficas com Java Swing.
* Operações CRUD (*Create, Read, Update, Delete*).
* Integração com banco de dados relacional utilizando JDBC.
* Relacionamentos entre entidades.
* Separação de responsabilidades por meio de classes e pacotes.
* Controle de versão com Git.

## Contexto

Projeto desenvolvido para fins acadêmicos, com foco na aplicação prática dos fundamentos de desenvolvimento de software.
