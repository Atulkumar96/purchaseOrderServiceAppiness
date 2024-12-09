# **Purchase Order Spring Boot Project - Help Guide**

This is a Spring Boot project integrated with the **Camunda Workflow Engine** for managing the purchase order workflow. Below are the steps to run and use the application.

## **How to Run the Application?**

1. **Clone the Project**:
    - First, clone the project to your desired location.

      ```bash
      git clone https://github.com/your-repo/purchase-order-workflow.git
      cd purchase-order-workflow
      ```

2. **Build the Project**:
    - Run the following command to build the project and generate a JAR file.

      ```bash
      mvn clean package
      ```

    - This will create a JAR file named `purchase-order-workflow-0.0.1-SNAPSHOT.jar` inside the `target/` folder.

3. **Run the Application**:
    - Once the JAR file is generated, run the following command to start the application.

      ```bash
      java -jar target/purchase-order-workflow-0.0.1-SNAPSHOT.jar
      ```

    - This will start the Spring Boot application along with the **Camunda engine instance**.

4. **Access the Camunda Dashboard**:
    - The application will be running on `http://localhost:8080` by default.
    - Open your browser and navigate to `http://localhost:8080` to access the **Camunda dashboard**.

5. **Camunda Workflow**:
    - The Camunda engine will use the **`purchase_order_workflow.bpmn`** file located inside the **`resources`** folder. This BPMN file defines the workflow for processing purchase orders.

---

## **How to Log into the Camunda Dashboard?**

1. **Login Credentials**:
    - **Username**: `demo` (default user)
    - **Password**: `demo` (default password)

2. **Admin User**:
    - The admin user has the ability to create new users and assign them to different roles.
    - Admin can create and assign users to groups such as **PUBLISHER**, **REVIEWER**, and **APPROVER**.

---

## **User Roles and Workflow Steps**

The application involves the following user roles:

- **PUBLISHER**: Initiates and fills the purchase order.
- **REVIEWER**: Reviews the purchase order and can request rework or approve/reject the order.
- **APPROVER**: Approves or rejects the purchase order.

### **Users Setup Example**:

1. **userx** - Role: `PUBLISHER`
2. **userr** - Role: `REVIEWER`
3. **usera** - Role: `APPROVER`

### **Workflow Execution**:

1. **PUBLISHER (userx)**:
    - Logs in to the Camunda dashboard.
    - In the **Camunda Tasklist**, the `PurchaseOrderProcess` task will appear.
    - Userx initiates the process by filling in the purchase order details (parts price, labor price, etc.) and submits it.

2. **REVIEWER (userr)**:
    - After submission, the reviewer (userr) can claim the task.
    - The reviewer reviews the purchase order details.
    - The reviewer can either approve the order, reject it, or request rework.

3. **APPROVER (usera)**:
    - The approver (usera) can approve or reject the purchase order once it has been reviewed.

---

## **Backend API Integration**

- The **Camunda Workflow** communicates with the backend APIs to create and update the purchase order.

- **API Endpoints**:
    - **POST** `/txn` - Create a new purchase order.
    - **PATCH** `/txn` - Update an existing purchase order.

These APIs interact with the **PurchaseOrderService** to manage purchase orders and review summaries.

---

## **Database Setup**

- You can use any database for storing the purchase orders and review summaries.
- Modify the database driver and connection details in the `application.yaml` file as per your database configuration.

### **PostgreSQL Sample Schema**

If you're using **PostgreSQL**, below are the SQL scripts to create the necessary tables:

```sql
-- Table for storing Purchase Orders
CREATE TABLE IF NOT EXISTS public.purchase_order
(
    record_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    task_id character varying(255) COLLATE pg_catalog."default",
    parts_price double precision,
    labour_price double precision,
    amount double precision,
    status character varying(50) COLLATE pg_catalog."default",
    created_by character varying(255) COLLATE pg_catalog."default",
    updated_by character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT purchase_order_pkey PRIMARY KEY (record_id)
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.purchase_order
    OWNER to postgres;

CREATE SEQUENCE purchase_order_review_summary_id_seq;

-- Table for storing Purchase Order Review Summaries
CREATE TABLE IF NOT EXISTS public.purchase_order_review_summary
(
    id integer NOT NULL DEFAULT nextval('purchase_order_review_summary_id_seq'::regclass),
    record_id character varying(255) COLLATE pg_catalog."default",
    review_comment character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT purchase_order_review_summary_pkey PRIMARY KEY (id),
    CONSTRAINT purchase_order_review_summary_record_id_fkey FOREIGN KEY (record_id)
        REFERENCES public.purchase_order (record_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.purchase_order_review_summary
    OWNER to postgres;
