# Functional-Reasoning

## Abstract

System and software design benefits greatly from formal modeling, allowing for automated analysis and verification early in the design phase. Current methods excel at checking information flow and component interactions, ensuring consistency, and identifying dependencies within Systems Modeling Language (SysML) models. However, these approaches often lack the capability to perform physics-based reasoning about a system’s behavior represented in SysML models, particularly in the electromechanical domain. This significant gap critically hinders the ability to automatically and effectively verify the correctness and consistency of the model’s behavior against well-established underlying physical principles. Therefore, this paper presents an approach that leverages existing research on function representation, including formal languages, graphical representations, and reasoning algorithms, and integrates them with physics-based verification techniques. Four case studies (coffeemaker, vacuum cleaner, hairdryer, and wired speaker) are inspected to illustrate the model’s practicality and effectiveness in performing physics-based reasoning on systems modeled in SysML. This automated physics-based reasoning is broken into two main categories: (i) structural, which is performed on BDD and IBD, and (ii) functional, which is then performed on activity diagrams. This work advances the field of automated reasoning by providing a framework for verifying structural and functional correctness and consistency with physical laws within SysML models.
 
## Table of Contents
01. [Requirements](#requirements)
02. [Installation](#installation)
03. [Usage](#usage)
04. [Contact](#contact)

## Requirements
### (a) System Model Type
The reasoner performs a series of physics-based inspectinos to the model input by the user. The structural and functional analysis relies on the system's physical components as well as the system's flow of materials and energies. For this reason, the reasoner was constructed to validate only electro-mechanical systems. For example, our case study tested an electric coffeemaker, a hair dryer, a vacuum cleaner, and a wired speaker.

### (b) SysML Diagrams
To apply the reasoner to a SysML model, the system must have at least a Block Definition Diagram (BDD) and an Internal Block Diagram (IBD) for each block with internal components. In addition, each IBD should only have one layer of internal nesting. For a more in-depth analysis, Activity Diagrams (AD) can be included to perform reasoning on functions. Note that without ADs, the reasoner is restricted to a structural analysis of the model. The specifications for ADs are elborated on in the requirements for the functional knowledge base.

### (c) Flow Type Vocabulary
To perform the balance laws inspection, the ports on blocks and properties must adhere to a specialized naming system. The string should begin with either "IN" or "OUT" to specify whether the flow is entering or exiting a component. The next segment of the string should define the type of flow being transferred. See Table 1 and Table 2 for a list of accepted abbreviations that correspond to each material and energy flow. Ensure also that the flow direction and flow type are separated in the string by an underscore. Additionally, the port names are not case sensitive and may contain numeric values to maintain singularity.

<em>Table 1: Material Flow Abbreviations</em>

<img src="https://github.com/user-attachments/assets/2a2564be-bd05-485c-ad34-78d922bb3862" width="180" alt="Material Abbr. Table"> <p>  </p>

<em>Table 2: Energy Flow Abbreviations</em> 

<img src="https://github.com/user-attachments/assets/b23ebc0d-da2f-4dee-bd5a-c027eee0baf5" width="280" alt="Energy Abbr. Table">


### (d) Functional Knowledge Base


## Installation


## Usage


## Contact
For questions, suggestions, or collaborations:
- **Candice Chambers** - chambersc2017@my.fit.edu
- **Summer Mueller** - smueller2023@my.fit.edu
- **Parth Ganeriwala** - pganeriwala2022@my.fit.edu
